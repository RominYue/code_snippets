##用法
###1 设置jni动态库所在的路径

```bash
$ export LD_LIBRARY_PATH="/your-path/lib": $LD_LIBRARY_PATH
```

###2 导入ltp4j的jar包写一般的程序

###3 命令行编译运行
```bash
$ $javac -cp /your-path/ltp4j.jar xxx.java
$ $java -cp .:/your-path/ltp4j.jar 包名.类名
```