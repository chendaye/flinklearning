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