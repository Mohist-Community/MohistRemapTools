package red.mohist.mohistremaptools;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// 将craftbukkit的nms-patches文件夹中的所有文件名重命名为mcp
public class CraftBukkitPatches2MCP {

    public static Map<String, String> cl = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        String str;

        File map = new File("cache", "Spigot2Mcp-1.16.3.srg");
        BufferedReader b = new BufferedReader(new InputStreamReader(FileUtil.openInputStream(map)));
        while ((str = b.readLine()) != null) {
            if (str.startsWith("CL: ") && !str.contains("$")) {
                String s = str.replace("CL: ", "");
                String[] strings = s.split(" ");
                String nms = strings[0];
                String mcp = strings[1];

                String[] nmss = nms.split("/");
                String[] mcps = mcp.split("/");

                String nms_classname = nmss[nmss.length - 1];
                String mcps_classname = mcps[mcps.length - 1];
                // 存入map
                cl.put(nms_classname, mcps_classname);
            }
        }

        StringBuilder sb = new StringBuilder();
        cl.forEach((key, value) -> sb.append(key).append(" - ").append(value).append("\n"));
        File file = new File("cache", "cb2mcp_patches.srg");
        Spigot2Mcp.writeByteArrayToFile(file, sb);
        System.out.println("成功保存" + cl.size() + "条数据");

        // rename
        for (File file1 : getFileList("nms-patches")) {
            String oldname = file1.getName().replace(".patch", "");
            String newname = cl.get(oldname);
            if (file1.renameTo(new File("nms-patches", newname + ".patch"))) {
                System.out.println("成功将 " + oldname + " -> " + newname);
            }

        }
    }

    /**
     * @description 使用递归的方法调用，并判断文件名是否以.jpg结尾
     * @param path 文件夹路径
     * @return java.util.List<java.io.File>
     * @author https://blog.csdn.net/chen_2890
     * @date 2019/6/14 17:35
     * @version V1.0
     */
    public static List<File> getFileList(String path) {
        List<File> fileList = new ArrayList<>();
        File dir = new File(path);
        // 该文件目录下文件全部放入数组
        File[] files = dir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                // 判断是文件还是文件夹
                if (files[i].isDirectory()) {
                    // 获取文件绝对路径
                    getFileList(files[i].getAbsolutePath());
                    // 判断文件名是否以.jpg结尾
                } else if (fileName.endsWith(".patch")) {
                    String strFileName = files[i].getAbsolutePath();
                    System.out.println("---" + strFileName);
                    fileList.add(files[i]);
                } else {
                    continue;
                }
            }
        }
        return fileList;
    }
}
