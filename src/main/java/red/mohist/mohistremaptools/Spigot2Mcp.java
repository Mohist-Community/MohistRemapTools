package red.mohist.mohistremaptools;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Spigot2Mcp {

    public static Map<String, String> spigot_cl = new ConcurrentHashMap<>();
    public static Map<String, String> spigot_fd = new ConcurrentHashMap<>();
    public static Map<String, String> spigot_md = new ConcurrentHashMap<>();

    public static Map<String, String> obf_cl = new ConcurrentHashMap<>();
    public static Map<String, String> obf_fd = new ConcurrentHashMap<>();
    public static Map<String, String> obf_md = new ConcurrentHashMap<>();

    public static Map<String, String> new_cl = new ConcurrentHashMap<>();
    public static Map<String, String> new_fd = new ConcurrentHashMap<>();
    public static Map<String, String> new_md = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        String str;


        File spigot = new File("cache", "spigot2obf.srg");
        File obf = new File("cache", "mcpconfig-1.16.3-joined.srg");


        System.out.println("==========================================");
        System.out.println("spigot2obf文件读取");
        BufferedReader b = new BufferedReader(new InputStreamReader(FileUtil.openInputStream(spigot)));
        while ((str = b.readLine()) != null) {
            if (str.startsWith("CL: ")) {
                String s = str.replace("CL: ", "");
                String[] strings = s.split(" ");
                spigot_cl.put(strings[0], strings[1]);
            }

            if (str.startsWith("FD: ")) {
                String s = str.replace("FD: ", "");
                String[] strings = s.split(" ");
                spigot_fd.put(strings[0], strings[1]);
            }

            if (str.startsWith("MD: ")) {
                String s = str.replace("MD: ", "");
                String[] strings = s.split(" ");
                spigot_md.put(strings[0] + " " + strings[1], strings[2] + " " + strings[3]);
            }
        }
        System.out.println(spigot_cl.size());
        System.out.println(spigot_fd.size());
        System.out.println(spigot_md.size());

        System.out.println("总计： " + (spigot_cl.size() + spigot_fd.size() + spigot_md.size()));
        System.out.println("==========================================");
        System.out.println("mcpconfig-1.16.3-joined.srg文件读取");
        BufferedReader b1 = new BufferedReader(new InputStreamReader(FileUtil.openInputStream(obf)));
        while ((str = b1.readLine()) != null) {
            if (str.startsWith("CL: ")) {
                String s = str.replace("CL: ", "");
                String[] strings = s.split(" ");
                obf_cl.put(strings[0], strings[1]);
            }

            if (str.startsWith("FD: ")) {
                String s = str.replace("FD: ", "");
                String[] strings = s.split(" ");
                obf_fd.put(strings[0], strings[1]);
            }

            if (str.startsWith("MD: ")) {
                String s = str.replace("MD: ", "");
                String[] strings = s.split(" ");
                obf_md.put(strings[0] + " " + strings[1], strings[2] + " " + strings[3]);
            }
        }
        System.out.println(obf_cl.size());
        System.out.println(obf_fd.size());
        System.out.println(obf_md.size());

        System.out.println("总计： " + (obf_cl.size() + obf_fd.size() + obf_md.size()));
        System.out.println("==========================================");
        System.out.println("开始合并CL数据");
        StringBuilder sb = new StringBuilder();
        spigot_cl.forEach((nms, sobf) -> obf_cl.forEach((oobf, mcp) -> {
            if (sobf.equals(oobf)) {
                new_cl.put(nms, mcp);
                sb.append("CL: ").append(nms).append(" ").append(mcp).append("\n");
            }
        }));
        System.out.println("CL数据合并完成");
        System.out.println("开始合并FD数据");
        spigot_fd.forEach((nms, sobf) -> obf_fd.forEach((oobf, mcp) -> {
            if (sobf.equals(oobf)) {
                new_fd.put(nms, mcp);
                sb.append("FD: ").append(nms).append(" ").append(mcp).append("\n");
            }
        }));
        System.out.println("FD数据合并完成");
        System.out.println("开始合并MD数据");
        spigot_md.forEach((nms, sobf) -> obf_md.forEach((oobf, mcp) -> {
            if (sobf.equals(oobf)) {
                new_md.put(nms, mcp);
                sb.append("MD: ").append(nms).append(" ").append(mcp).append("\n");
            }
        }));
        System.out.println("MD数据合并完成");

        File file = new File("cache", "Spigot2Mcp-1.16.3.srg");
        writeByteArrayToFile(file, sb);
    }

    public static void writeByteArrayToFile(File file, StringBuilder sb){
        try {
            FileUtil.writeData(file, sb.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
