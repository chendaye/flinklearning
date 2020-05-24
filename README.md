# 多模块项目参考

[多模块](https://juejin.im/post/5da80629e51d4524a4307641)

[maven 继承](https://juejin.im/post/5da947535188253fab49c9fa)

[maven 聚合](https://juejin.im/post/5da963c4e51d4524994844e2)

# 新建多模块项目步骤

- 新建maven项目
- 删掉src
- 新建Module；过程与新建maven类似

# 什么是多模块
> Maven首先在当前构建项目的环境中查找父pom，然后项目所在的文件系统查找，然后是本地存储库，最后是远程repo。

> 在创建多个模块之后，可以在父pom中添加公共配置，然后所有的子模块都会继承这些配置。除此之外，还可以通用对子模块进行 编译、打包、安装... 操作


> 依赖版本的查找
  Maven会沿着父子层次向上走，直到找到一个拥有 dependencyManagement 组件的项目，然后在其中查找，如果找到则返回申明的依赖，没有继续往下找

> dependencies 与 dependencyManagement 的区别

dependencies
- 引入依赖
- 即使子项目中不写 dependencies ，子项目仍然会从父项目中继承 dependencies 中的所有依赖项

dependencyManagement
- 声明依赖，并不引入依赖。
- 子项目默认不会继承父项目 dependencyManagement 中的依赖
- 只有在子项目中写了该依赖项，并且没有指定具体版本，才会从父项目中继承（version、exclusions、scope等读取自父pom）
- 子项目如果指定了依赖的具体版本号，会优先使用子项目中指定版本，不会继承父pom中申明的依赖

# 子模块之间相互引用

```
先将 父 pom install
再将子 pom install

之后再打包 package，引用才不会有问题
```

[pom 介绍](https://zhuanlan.zhihu.com/p/76874769)


# Scope

````$xslt
compile:
This is the default scope, used if none is specified. Compile dependencies are available in all classpaths of a project. Furthermore, those dependencies are propagated to dependent projects.
（未指定scope时默认使用该项。项目始终依赖使用该jar包。会传递依赖的项目）

provided:
This is much like compile, but indicates you expect the JDK or a container to provide the dependency at runtime. For example, when building a web application for the Java Enterprise Edition, you would set the dependency on the Servlet API and related Java EE APIs to scope provided because the web container provides those classes. This scope is only available on the compilation and test classpath, and is not transitive.
（与compile类似，但表明你期望JDK或容器在运行时提供的依赖。例如，当构建一个Web应用程序的java企业版，你将依赖Servlet API和相关的java EE API提供的Web容器提供的范围，用于编译和测试阶段，不具有传递性。jar包不会包含依赖项）

runtime:
This scope indicates that the dependency is not required for compilation, but is for execution. It is in the runtime and test classpaths, but not the compile classpath.
（表示被依赖项目不会参与项目的编译，但用于运行和测试。与compile相比，跳过了编译这个环节）

test:
This scope indicates that the dependency is not required for normal use of the application, and is only available for the test compilation and execution phases. This scope is not transitive.
（此范围表示应用程序的正常使用不需要依赖项，只适用于测试编译和执行阶段。不具有传递性。）

system:
This scope is similar to provided except that you have to provide the JAR which contains it explicitly. The artifact is always available and is not looked up in a repository.
（与provided相似，但必须我们给它提供显式JAR包。不会从maven远程中央仓库下载，而是从本地Maven仓库中获取。）

import (only available in Maven 2.0.9 or later)：
This scope is only supported on a dependency of type pom in the section. It indicates the dependency to be replaced with the effective list of dependencies in the specified POM’s section. Since they are replaced, dependencies with a scope of import do not actually participate in limiting the transitivity of a dependency.
（import scope只能用在dependencyManagement里面。表示从其它的pom中导入dependency的配置。）


事情是这样的：
第一次，我在本地写的spark代码需要提到spark集群上运行，集群中一运行就报某个类找不到（没有打包到jar中）。
原因是因为我把scope的配置项写成了provided，这样打成的包中根本没有依赖的jar，将scope依赖项注释掉，再重新打包，提交集群运行就没问题了。

第二次，我是写的spark sql的代码在本地运行，运行时还是报某个类找不到。
同样这次配置还是provided，就是本地运行，不打包，不碍事吧应该，但还是找不到类。同样的配置在eclipse中运行正常，IDEA中就是不行。

````