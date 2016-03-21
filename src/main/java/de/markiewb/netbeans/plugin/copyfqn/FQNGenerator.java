/**
 * Copyright 2013 markiewb
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package de.markiewb.netbeans.plugin.copyfqn;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import static de.markiewb.netbeans.plugin.copyfqn.CopyFQNPanel.LONGMODE_DEFAULT;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.lang.model.element.Element;
import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.ElementKind.CONSTRUCTOR;
import static javax.lang.model.element.ElementKind.ENUM;
import static javax.lang.model.element.ElementKind.ENUM_CONSTANT;
import static javax.lang.model.element.ElementKind.FIELD;
import static javax.lang.model.element.ElementKind.INTERFACE;
import static javax.lang.model.element.ElementKind.METHOD;
import static javax.lang.model.element.ElementKind.PACKAGE;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.QualifiedNameable;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import org.netbeans.api.java.source.CompilationController;
import org.netbeans.api.java.source.CompilationInfo;
import org.netbeans.api.java.source.JavaSource;
import org.netbeans.api.java.source.Task;
import org.netbeans.api.java.source.TreePathHandle;
import org.netbeans.api.java.source.TypeUtilities;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;

/**
 *
 * @author benno.markiewicz@googlemail.com
 */
public final class FQNGenerator {

    /**
     * Get all fully qualified names for the given {@link TreePathHandle}s.
     *
     * @param handles
     * @return
     * @throws IllegalArgumentException
     */
    public static List<String> getAllFQNFor(Collection<? extends TreePathHandle> handles) {
        final List<String> resultList = new ArrayList<>();

        //support multiple selections
        for (final TreePathHandle handle : handles) {
            final FileObject fileObject = handle.getFileObject();

            if (null == fileObject) {
                continue;
            }
            JavaSource javaSource = JavaSource.forFileObject(fileObject);

            final List<String> javaResultList = new ArrayList<>();
            if (null != javaSource) {
                //recognized java source
                try {
                    Task<CompilationController> task = new Task<CompilationController>() {
                        @Override
                        public void run(CompilationController compilationController) throws Exception {
                            compilationController.toPhase(JavaSource.Phase.PARSED);

                            //collects fqn if supported
                            final AbstractMemberVisitor visitor = new AbstractMemberVisitor(compilationController) {
                                @Override
                                public Element getElement(CompilationInfo info) {
                                    TreePath pathFor = handle.resolve(info);
                                    if (null != pathFor) {
                                        return info.getTrees().getElement(pathFor);
                                    } else {
                                        return null;
                                    }
                                }
                            };
                            visitor.scan(compilationController.getCompilationUnit(), null);
                            javaResultList.addAll(visitor.getResultList());
                        }
                    };
                    javaSource.runUserActionTask(task, true);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            resultList.addAll(javaResultList);
        }
        return resultList;
    }

    /**
     * Collects fully qualified names of selected items. If an item is supported
     * then its fully qualified name will be accessible via
     * {@link #getResultList()}.
     */
    public static abstract class AbstractMemberVisitor extends TreePathScanner<Void, Void> {

        /**
         * @return previously collected fully qualified names of n-items
         */
        public List<String> getResultList() {
            return new ArrayList<>(resultList);
        }
        private CompilationInfo info;
        private List<String> resultList;

        public AbstractMemberVisitor(CompilationInfo info) {
            this.info = info;
            resultList = new ArrayList<>();
        }

        @Override
        public Void visitCompilationUnit(CompilationUnitTree t, Void v) {
            Element e = getElement(info);
            if (null == e) {
                return null;
            }
            final Element enclosingElement = e.getEnclosingElement();
            String result = getFQN(enclosingElement, e);
            if (null != result) {
                resultList.add(result);
            }
            return null;
        }

        /**
         * Get the FQN of the parameters of an executableElement.
         *
         * @param executableElement method or constructor
         * @return
         */
        private List<String> getParameters(ExecutableElement executableElement) {
            final List<String> result = new ArrayList<>();
            for (VariableElement variableElement : executableElement.getParameters()) {
                String typeName = formatTypeMirror(variableElement.asType());
                result.add(typeName);
            }
            return result;
        }

        private String formatTypeMirror(final TypeMirror type) {
            String typeName;
            boolean longMode = NbPreferences.forModule(CopyFQNPanel.class).getBoolean("longMode", LONGMODE_DEFAULT);
            if (longMode) {
                typeName = info.getTypeUtilities().getTypeName(type, TypeUtilities.TypeNameOptions.PRINT_FQN).toString();
            } else {
                typeName = info.getTypeUtilities().getTypeName(type).toString();
            }
            return formatType(typeName);
        }

        private String getFQN(final Element enclosingElement, Element e) {
            String result;
            {
                switch (e.getKind()) {
                    case CONSTRUCTOR: {
                        if (!(enclosingElement instanceof QualifiedNameable)) {
                            return null;
                        }
                        String fqType = ((QualifiedNameable) enclosingElement).getQualifiedName().toString();
                        ExecutableElement executableElement = (ExecutableElement) e;
                        final String constructorName = ((QualifiedNameable) enclosingElement).getSimpleName().toString();
                        final List<String> params = getParameters(executableElement);
                        result = String.format("%s.%s(%s)", formatType(fqType), constructorName, StringUtils.join(params, ", "));
                    }
                    break;
                    case METHOD: {
                        if (!(enclosingElement instanceof QualifiedNameable)) {
                            return null;
                        }
                        String fqType = ((QualifiedNameable) enclosingElement).getQualifiedName().toString();
                        final ExecutableElement executableElement = (ExecutableElement) e;
                        final String methodName = executableElement.getSimpleName().toString();
                        final List<String> params = getParameters(executableElement);
                        //ignore return type - not supported in eclipse
                        //final String returnType = getReturnType(executableElement);
                        final String returnType = "";
                        result = String.format("%s.%s(%s)%s", formatType(fqType), methodName, StringUtils.join(params, ", "), returnType);
                    }
                    break;
                    case CLASS:
                    //fallthrough
                    case ENUM:
                    //fallthrough
                    case ANNOTATION_TYPE:
                    //fallthrough
                    case INTERFACE:
                        if (!(enclosingElement instanceof QualifiedNameable)) {
                            return null;
                        }
                        String fqType = ((QualifiedNameable) enclosingElement).getQualifiedName().toString();

                        //support fields, toplevel-classes/-enums/-interfaces
                        result = String.format("%s.%s", formatType(fqType), e.getSimpleName());
                        break;
                    case PACKAGE:
                        result = nameFor((PackageElement) e);
                        break;
                    case ENUM_CONSTANT:
                        result = nameFor((VariableElement) e);
                        break;
                    case FIELD:
                        result = nameFor((VariableElement) e);
                        break;
                    default:
                        result = null;
                }
            }
            return result;
        }

        /**
         * Returns the FQN for a {@link VariableElement}.
         *
         * @param variableElement
         * @return
         */
        String nameFor(VariableElement variableElement) {
            String surroundingElement = "";
            String variableName = variableElement.toString();
            if (variableElement.getEnclosingElement() instanceof TypeElement) {
                surroundingElement = nameFor((TypeElement) variableElement.getEnclosingElement());
            }
            if (!"".equals(surroundingElement)) {
                return surroundingElement + "." + variableName;
            } else {
                return variableName;
            }
        }

        /**
         * Returns the FQN for a {@link TypeElement}.
         *
         * @param typeElement
         * @return
         */
        String nameFor(TypeElement typeElement) {
            return typeElement.getQualifiedName().toString();
        }

        /**
         * Returns the FQN for a {@link PackageElement}.
         *
         * @param packageElement
         * @return
         */
        String nameFor(PackageElement packageElement) {
            return packageElement.toString();
        }

        public abstract Element getElement(CompilationInfo info);
    }

    public static String formatType(final String typeName) {

        if (null == typeName || typeName.isEmpty()) {
            return "";
        }
        if (typeName.startsWith("java.lang.")) {
            return typeName.substring("java.lang.".length());
        }
        return typeName;
    }

}
