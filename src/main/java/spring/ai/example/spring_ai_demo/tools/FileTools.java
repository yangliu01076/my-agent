package spring.ai.example.spring_ai_demo.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

/**
 * @author duoyian
 * @since 2026/8/12
 */
@Component
public class FileTools {

    private final String BASE_DIR = "/Users/duoyian/IdeaProjects/spring-ai-demo/files";

    // @Tool 注解描述了这个工具的功能
    // description 里的内容非常重要，AI 会根据它来决定是否调用这个工具
    @Tool(description = "读取指定路径文件的内容，参数为文件名")
    public String readFile(String filename) {
        System.out.println("读取文件: " + filename);
        try {
            Path path = Paths.get(BASE_DIR, filename);
            if (!Files.exists(path)) {
                return "错误: 文件 " + filename + " 不存在。";
            }
            return Files.readString(path);
        } catch (IOException e) {
            return "读取文件失败: " + e.getMessage();
        }
    }

    @Tool(description = "将内容写入到指定文件中。参数：filename(文件名), content(内容)")
    public String writeFile(String filename, String content) {
        System.out.println("写入文件: " + filename);
        try {
            Path dir = Paths.get(BASE_DIR);
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
            Path path = dir.resolve(filename);
            Files.writeString(path, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return "成功: 文件 " + filename + " 已保存。";
        } catch (IOException e) {
            return "写入文件失败: " + e.getMessage();
        }
    }

    @Tool(description = "列出当前工作目录 'files' 下所有的文件名")
    public String listFiles() {
        System.out.println("列出文件");
        try {
            Path dir = Paths.get(BASE_DIR);
            if (!Files.exists(dir)) {
                return "当前没有文件目录。";
            }
            List<String> files = Files.list(dir).map(p -> p.getFileName().toString()).toList();
            if (files.isEmpty()) return "目录为空。";
            return String.join(", ", files);
        } catch (IOException e) {
            return "列出文件失败: " + e.getMessage();
        }
    }

    @Tool(description = "在工作目录 'files' 下创建一个新的文件夹。参数 name 是要创建的文件夹名称")
    public String createDirectory(String name) {
        System.out.println("创建文件夹: " + name);
        try {
            // 定义基础路径，对应之前的 BASE_DIR = "files/"
            Path basePath = Paths.get(BASE_DIR);

            // 确保基础目录存在
            if (!Files.exists(basePath)) {
                Files.createDirectories(basePath);
            }

            // 构建新文件夹的完整路径
            Path newDir = basePath.resolve(name);

            // 检查是否已经存在
            if (Files.exists(newDir)) {
                if (Files.isDirectory(newDir)) {
                    return "文件夹 '" + name + "' 已经存在了。";
                } else {
                    return "错误：存在一个同名的文件 '" + name + "'，无法创建文件夹。";
                }
            }

            // 创建文件夹
            Files.createDirectory(newDir);

            return "成功创建文件夹: " + name;

        } catch (IOException e) {
            return "创建文件夹失败: " + e.getMessage();
        }
    }

    /**
     * 复制文件（支持递归路径，目标文件夹不存在会自动创建）
     */
    @Tool(description = "将源文件复制到目标位置。支持递归路径。如果目标路径中的文件夹不存在会自动创建。参数：sourceFilename(源文件路径), targetFilename(目标文件路径)")
    public String copyFile(String sourceFilename, String targetFilename) {
        System.out.println("复制文件: " + sourceFilename + " -> " + targetFilename);
        try {
            Path basePath = Paths.get(BASE_DIR);

            // 1. 解析源路径（规范化路径，处理 .. 或 . 等符号）
            Path sourcePath = basePath.resolve(sourceFilename).normalize();
            Path targetPath = basePath.resolve(targetFilename).normalize();

            // 2. 基础检查
            if (!Files.exists(sourcePath)) {
                return "错误: 源文件 " + sourceFilename + " 不存在。";
            }
            if (!Files.isRegularFile(sourcePath)) {
                return "错误: 源路径 " + sourceFilename + " 不是一个有效的文件。";
            }

            // 3. 检查目标是否是已存在的目录（防止将文件复制到一个文件夹里但没有指定新文件名）
            if (Files.exists(targetPath) && Files.isDirectory(targetPath)) {
                return "错误: 目标路径 " + targetFilename + " 是一个已存在的文件夹。请指定完整的目标文件名（例如 'backup/' -> 'backup/file.txt'）。";
            }

            // 4. 【关键】递归创建目标文件的父目录
            // 例如 target 是 'backup/2023/data.txt'，我们需要确保 'backup/2023/' 存在
            Path targetParentDir = targetPath.getParent();
            if (targetParentDir != null && !Files.exists(targetParentDir)) {
                Files.createDirectories(targetParentDir);
            }

            // 5. 执行复制（覆盖已存在的文件）
            // StandardCopyOption.REPLACE_EXISTING: 如果目标文件已存在，则覆盖
            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

            return "成功: 已将 " + sourceFilename + " 复制为 " + targetFilename + "。";

        } catch (IOException e) {
            return "复制文件失败: " + e.getMessage();
        }
    }
}
