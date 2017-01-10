This plugin allows you to copy the fully qualified name of the element at the caret in the java source editor to the clipboard. There is also a context menu entry to copy it from the members and hierarchy view. Shortcuts for these actions can also be defined.

<p>
<ul>
<li>Copy qualified name (in Java editor): <code>CTRL-J, CTRL-C</code></li>
<li>Copy qualified name (in Members/Hierarchy view): <code>CTRL-J, CTRL-I</code></li>
</ul>
</p>
<img src="https://raw.githubusercontent.com/markiewb/nb-copy-fqn/master/src/main/java/de/markiewb/netbeans/plugin/copyfqn/example.png">
<p>
<h2>Updates</h2>

<h3>1.4.1:</h3>
<ul>
<li>[<a href="https://github.com/markiewb/nb-copy-fqn/issues/12">Bug</a>]: Built artefacts have wrong name</li>
 </ul>

<h3>1.4.0:</h3>
<ul>
<li>[<a href="https://github.com/markiewb/nb-copy-fqn/issues/10">Feature</a>]: Don't use FQN for types from java.lang</li>
 </ul>

<h3>1.3.0.3:</h3>
<ul>
<li>[<a href="https://github.com/markiewb/nb-copy-fqn/issues/5">Feature</a>]: Shorten up FQN (eclipse like, enabled by default) and option dialog</li>
<li>[<a href="https://github.com/markiewb/nb-copy-fqn/issues/4">Feature</a>]: Support annotation types too</li>
<li>[<a href="https://github.com/markiewb/nb-copy-fqn/issues/7">Bug</a>]: Support generics in parameters</li>
<li>[<a href="https://github.com/markiewb/nb-copy-fqn/issues/8">Bug</a>]: Do not copy return type of methods (keep it compatible to eclipse)</li>
<li>[<a href="https://github.com/markiewb/nb-copy-fqn/issues/1">Task</a>]: Convert to maven-based project</li>
<li>[<a href="https://github.com/markiewb/nb-copy-fqn/issues/5">Task</a>]: Add donation link </li>
 </ul>
<h3>1.2</h3>
<ul>
<li>Fix: runnable in JDK6/JDK7</li>
</ul>
<h3>1.1</h3>
<ul>
<li><a href="http://code.google.com/p/copyfqn-fork-for-netbeans71rc2/issues/detail?id=5">Issue 5:</a> Attach action into context menu of members and hierarchy view (&ge; NB 7.3)</li> 
<li>some internal rewrites</li>
</ul>
</p>

<p>The code is originally based on the tutorials
<a href="http://platform.netbeans.org/tutorials/nbm-copyfqn.html">[1]</a> and
<a href="http://netbeans.org/projects/platform/sources/platform-content/content/trunk/tutorials/60/nbm-whichelement.html?raw=true">[2]</a>.

Regarding to <a href="http://wiki.netbeans.org/TutorialsInCourses">[3]</a> the code from
tutorials is free and <a href="http://netbeans.org/about/legal/terms-of-use.html">[4]</a> 4.c
applies, so that this derivate can be licensed separately.</p>

<p>
Provide defects, request for enhancements and feedback at <a href=https://github.com/markiewb/nb-copy-fqn/issues">https://github.com/markiewb/nb-copy-fqn/issues</a>
</p>
<p>Compatible to NetBeans 7.4+ and JDK7+</p>
<p>
<a href="https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=K4CMP92RZELE2"><img src="https://www.paypalobjects.com/en_US/i/btn/btn_donate_SM.gif" alt="btn_donate_SM.gif"></a>

</p>