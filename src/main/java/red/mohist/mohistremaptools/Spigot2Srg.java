package red.mohist.mohistremaptools;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Spigot2Srg {

    public static Map<String, String> oldmcp2srg_fd = new ConcurrentHashMap<>();
    public static Map<String, String> oldmcp2srg_md = new ConcurrentHashMap<>();

    public static Map<String, String> newmcp2srg_cl = new ConcurrentHashMap<>();
    public static Map<String, String> newmcp2srg_fd = new ConcurrentHashMap<>();
    public static Map<String, String> newmcp2srg_md = new ConcurrentHashMap<>();

    public static void main (String[] run) throws IOException {
        String str;
        System.out.println("==========================================");
        System.out.println("mcp.json文件读取");
        File mcp_json = new File("cache", "mcp.json");
        JsonElement root = new JsonParser().parse(new InputStreamReader(FileUtil.openInputStream(mcp_json)));

        System.out.println("读取: fields");
        // fields
        String json_fields = root.getAsJsonObject().get("fields").toString().replace("{", "").replace("}", "");
        String[] jsons_fields = json_fields.split(",");
        for (String s : jsons_fields) {
            String[] strings = s.split(":");
            String mcp = strings[0].replace("\"", "");
            String srg = strings[1].replace("\"", "");
            oldmcp2srg_fd.put(mcp, srg);
        }

        System.out.println("读取: methods");
        // methods
        String json_methods = root.getAsJsonObject().get("methods").toString().replace("{", "").replace("}", "");
        String[] jsons_methods = json_methods.split(",");
        for (String s : jsons_methods) {
            String[] strings = s.split(":");
            String mcp = strings[0].replace("\"", "");
            String srg = strings[1].replace("\"", "");
            oldmcp2srg_md.put(mcp, srg);
        }

        System.out.println("==========================================");
        System.out.println("Spigot2Mcp-1.16.3.srg文件读取");
        File map = new File("cache", "Spigot2Mcp-1.16.3.srg");
        BufferedReader b = new BufferedReader(new InputStreamReader(FileUtil.openInputStream(map)));
        while ((str = b.readLine()) != null) {
            if (str.startsWith("CL: ")) {
                String s = str.replace("CL: ", "");
                String[] strings = s.split(" ");
                newmcp2srg_cl.put(strings[0], strings[1]);
            }

            if (str.startsWith("FD: ")) {
                String s = str.replace("FD: ", "");
                String[] strings = s.split(" ");
                String[] strings1 = strings[1].split("/");
                String oldmcp = strings1[strings1.length - 1];
                if (oldmcp2srg_fd.containsKey(oldmcp)) {
                    String newmcp = oldmcp2srg_fd.get(oldmcp);
                    newmcp2srg_fd.put(strings[0], strings[1].replace(oldmcp, newmcp));
                } else {
                    newmcp2srg_fd.put(strings[0], strings[1]);
                }
            }

            if (str.startsWith("MD: ")) {
                String s = str.replace("MD: ", "");
                String[] strings = s.split(" ");

                // mcp name
                String[] strings1 = strings[2].split("/");
                String oldmcp = strings1[strings1.length - 1];

                if (oldmcp2srg_md.containsKey(oldmcp)) {
                    String newmcp = oldmcp2srg_md.get(oldmcp);
                    newmcp2srg_md.put(strings[0] + " " + strings[1], strings[2].replace(oldmcp, newmcp) + " " + strings[3]);
                } else {
                    newmcp2srg_md.put(strings[0] + " " + strings[1], strings[2] + " " + strings[3]);
                }
            }

        }
        System.out.println("==========================================");
        System.out.println("开始合并CL数据");
        StringBuilder sb = new StringBuilder();
        newmcp2srg_cl.forEach((nms, srg) -> sb.append("CL: ").append(nms).append(" ").append(srg).append("\n"));
        System.out.println("CL数据合并完成");
        System.out.println("开始合并FD数据");
        newmcp2srg_fd.forEach((nms, srg) -> sb.append("FD: ").append(nms).append(" ").append(srg).append("\n"));
        System.out.println("FD数据合并完成");
        System.out.println("开始合并MD数据");
        newmcp2srg_md.forEach((nms, srg) -> sb.append("MD: ").append(nms).append(" ").append(srg).append("\n"));
        System.out.println("==========================================");
        System.out.println("开始生成spigot2srg.srg");
        File file = new File("cache", "spigot2srg.srg");
        Spigot2Mcp.writeByteArrayToFile(file, sb);
    }
}
