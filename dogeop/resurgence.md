# MCBBS的一个帖子(已关闭)

#### 通过百度快照重新整理了一遍

1. 使用工具：
    - SpecialSource, Srg2source, forge MDK, MCP，[MinecraftMappings](https://github.com/Mohist-Community/MinecraftMappings)
#### 步骤：
1. 前期准备：Mac／linux环境（讲解都是基于这个），JDK,apache Maven,kotlin,eclipse/IDEA 
2. git clone https://github.com/md-5/SpecialSource, 进入克隆的代码库根目录，执行mvn package,取出target/SpecialSource-xxx-shaded.jar备用 
3. git clone https://github.com/Mohist-Community/MinecraftMappings, 使用IDEA运行main.kt文件 
4. 使用Spigot的BuildTools获取spigot-<1.x.x>.jar 
5. 将craftbukkit拷贝到你的工作目录，这里假定 MinecraftMappings/1.16.2/ 将之前的SpecialSource拷贝到这里。 
        
        用记事本编辑以下内容： 
        
        PK: net/minecraft/server/v1_16_R1 net/minecraft/server 
        PK: org/bukkit/craftbukkit/v1_16_R1 org/bukkit/craftbukkit 
        PK: org/bukkit/craftbukkit/libs/joptsimple joptsimple 
        PK: org/bukkit/craftbukkit/libs/joptsimple/internal joptsimple/internal 
        PK: org/bukkit/craftbukkit/libs/joptsimple/util joptsimple/util 
        PK: org/bukkit/craftbukkit/libs/jline jline 
        PK: org/bukkit/craftbukkit/libs/jline/console jline/console
        PK: org/bukkit/craftbukkit/libs/jline/console/compeleter jline/console/completer
        PK: org/bukkit/craftbukkit/libs/jline/console/history jline/console/history
        PK: org/bukkit/craftbukkit/libs/jline/internal jline/internal
        PK: org/bukkit/craftbukkit/libs/jline/org/ org
        PK: org/bukkit/craftbukkit/libs/jline/org/gjt org/gjt
        PK: org/bukkit/craftbukkit/libs/jline/org/gjt/mm org/gjt/mm
        PK: org/bukkit/craftbukkit/libs/jline/org/gjt/mm/mysql org/gjt/mm/mysql
        
    根据实际情况你可能要把v1_16_R1替换成v1_17_R1,v1_17_R2等字符，并且把org/bukkit/craftbukkit/libs/下面所有的包都映射到根目录，如org/bukkit/craftbukkit/libs/jline jline,具体要查看jar包内容操作，大概就是这样。将调整好的内容存到工作目录取名packages.srg
6. 重映射jar：执行 java -jar SpecialSource-xxx-shade.jar -i spigot-xxx.jar -o remap.jar -m packages.srg, 就可以得到重映射的spigot
7. 从Spigot目录/Spigot/ 下面把src拷贝到工作目录（MinecraftMapping/你的版本，下面叫PWD), 备用
8. git clone https://github.com/MinecraftForge/Srg2Source 用IDEA/Eclipse 打开这个工程，新建2个运行选项，其中一个设定为这样 

    ![1](https://i.loli.net/2020/07/19/fAhix2pJF15bHL6.png)

    main选择RangeExtractor 三个参数从上到下是 源码目录 任意目录 输出映射文件位置，源码选择你拷贝的spigot源代码（这里我用了src/main/java的java)
    
    另一个为这样  
    ![1](https://i.loli.net/2020/07/19/nPT7eUGfwD15rso.png)  
    --srcRoot 仍然是spigot源码 --SrcRangeMap 为你上一个生成的映射文件,srgFiles选择同一目录下的
    spigot2mcp.srg,--lvRangemap选择同一个映射，--excFiles 的来源另讲 --outDir为输出源码目录。  
    
    旧版： 
    其中exc File最简单的生成方式是：MDK(https://files.minecraftforge.net ... e/index_1.16.2.html)，在forge下载页面选择MDK并解压，进入解压的mdk目录执行 ./gradlew setupDecompWorkSpace，执行完后到
    ~/.gradle/caches/de/oceanlabs/mcp/mcp_snapshot/20xx_xxxx/x.x.x/srgs/ 拷贝出mcp.exc到工作目录里面，然后将
    excFiles指定为它即可。
9. 先执行RangeExtractor 再执行RangeApplier, 就可以在工作目录的输出目录下找到重映射好的代码了！之后你可以将它导入forge工作空间，就是你之前下载的mdk, 然后好了么？并不～
10. 应用 Access TransFormer