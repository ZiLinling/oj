package com.xmut.onlinejudge.config;

import java.util.HashMap;
import java.util.Map;

public class LanguageConfig {

    private static final String[] defaultEnv = {"LANG=en_US.UTF-8", "LANGUAGE=en_US:en", "LC_ALL=en_US.UTF-8"};
    //c语言配置
    private static final Map<String, Object> c_language_config = new HashMap<String, Object>();
    //c++语言配置
    private static final Map<String, Object> cpp_language_config = new HashMap<String, Object>();
    //java语言配置
    private static final Map<String, Object> java_language_config = new HashMap<String, Object>();
    //python2语言配置
    private static final Map<String, Object> py2_language_config = new HashMap<String, Object>();
    //python3语言配置
    private static final Map<String, Object> py3_language_config = new HashMap<String, Object>();
    //go语言配置
    private static final Map<String, Object> go_language_config = new HashMap<String, Object>();
    //php语言配置
    private static final Map<String, Object> php_language_config = new HashMap<String, Object>();
    //js语言配置
    private static final Map<String, Object> js_language_config = new HashMap<String, Object>();

    static {
        Map<String, Object> compile_config = new HashMap<String, Object>();
        compile_config.put("src_name", "main.c");
        compile_config.put("exe_name", "main");
        compile_config.put("max_cpu_time", 3000);
        compile_config.put("max_real_time", 5000);
        compile_config.put("max_memory", 128 * 1024 * 1024);
        compile_config.put("compile_command", "/usr/bin/gcc -DONLINE_JUDGE -O2 -w -fmax-errors=3 -std=c99 {src_path} -lm -o {exe_path}");
        Map<String, Object> run_config = new HashMap<String, Object>();
        run_config.put("command", "{exe_path}");
        run_config.put("seccomp_rule", "c_cpp");
        run_config.put("env", defaultEnv);
        c_language_config.put("compile", compile_config);
        c_language_config.put("run", run_config);
    }

    static {
        Map<String, Object> compile_config = new HashMap<String, Object>();
        compile_config.put("src_name", "main.cpp");
        compile_config.put("exe_name", "main");
        compile_config.put("max_cpu_time", 3000);
        compile_config.put("max_real_time", 5000);
        compile_config.put("max_memory", 128 * 1024 * 1024);
        compile_config.put("compile_command", "/usr/bin/g++ -DONLINE_JUDGE -O2 -w -fmax-errors=3 -std=c++11 {src_path} -lm -o {exe_path}");
        Map<String, Object> run_config = new HashMap<String, Object>();
        run_config.put("command", "{exe_path}");
        run_config.put("seccomp_rule", "c_cpp");
        run_config.put("env", defaultEnv);
        cpp_language_config.put("compile", compile_config);
        cpp_language_config.put("run", run_config);
    }

    static {
        Map<String, Object> compile_config = new HashMap<String, Object>();
        compile_config.put("src_name", "Main.java");
        compile_config.put("exe_name", "Main");
        compile_config.put("max_cpu_time", 3000);
        compile_config.put("max_real_time", 5000);
        compile_config.put("max_memory", -1);
        compile_config.put("compile_command", "/usr/bin/javac {src_path} -d {exe_dir} -encoding UTF8");
        Map<String, Object> run_config = new HashMap<String, Object>();
        run_config.put("command", "/usr/bin/java -cp {exe_dir} -XX:MaxRAM={max_memory}k -Djava.security.manager -Dfile.encoding=UTF-8 -Djava.security.policy==/etc/java_policy -Djava.awt.headless=true Main");
        run_config.put("seccomp_rule", null);
        run_config.put("env", defaultEnv);
        run_config.put("memory_limit_check_only", 1);
        java_language_config.put("compile", compile_config);
        java_language_config.put("run", run_config);
    }

    static {
        Map<String, Object> compile_config = new HashMap<String, Object>();
        compile_config.put("src_name", "solution.py");
        compile_config.put("exe_name", "solution.pyc");
        compile_config.put("max_cpu_time", 3000);
        compile_config.put("max_real_time", 5000);
        compile_config.put("max_memory", 128 * 1024 * 1024);
        compile_config.put("compile_command", "/usr/bin/python -m py_compile {src_path}");
        Map<String, Object> run_config = new HashMap<String, Object>();
        run_config.put("command", "/usr/bin/python {exe_path}");
        run_config.put("seccomp_rule", "general");
        run_config.put("env", defaultEnv);
        py2_language_config.put("compile", compile_config);
        py2_language_config.put("run", run_config);
    }

    static {
        Map<String, Object> compile_config = new HashMap<String, Object>();
        compile_config.put("src_name", "solution.py");
        compile_config.put("exe_name", "__pycache__/solution.cpython-36.pyc");
        compile_config.put("max_cpu_time", 3000);
        compile_config.put("max_real_time", 5000);
        compile_config.put("max_memory", 128 * 1024 * 1024);
        compile_config.put("compile_command", "/usr/bin/python3 -m py_compile {src_path}");
        Map<String, Object> run_config = new HashMap<String, Object>();
        run_config.put("command", "/usr/bin/python3 {exe_path}");
        run_config.put("seccomp_rule", "general");
        run_config.put("env", defaultEnv);
        py3_language_config.put("compile", compile_config);
        py3_language_config.put("run", run_config);
    }

    static {
        Map<String, Object> compile_config = new HashMap<String, Object>();
        compile_config.put("src_name", "main.go");
        compile_config.put("exe_name", "main");
        compile_config.put("max_cpu_time", 3000);
        compile_config.put("max_real_time", 5000);
        compile_config.put("max_memory", 1024 * 1024 * 1024);
        compile_config.put("compile_command", "/usr/bin/go build -o {exe_path} {src_path}");
        compile_config.put("env", "[\"GOCACHE=/tmp\", \"GOPATH=/tmp/go\"]");
        Map<String, Object> run_config = new HashMap<String, Object>();
        run_config.put("command", "{exe_path}");
        run_config.put("seccomp_rule", "");
        run_config.put("env", defaultEnv);
        run_config.put("memory_limit_check_only", 1);
        go_language_config.put("compile", compile_config);
        go_language_config.put("run", run_config);
    }

    static {
        Map<String, Object> run_config = new HashMap<String, Object>();
        run_config.put("exe_name", "solution.php");
        run_config.put("command", "/usr/bin/php {exe_path}");
        run_config.put("seccomp_rule", "");
        run_config.put("env", defaultEnv);
        run_config.put("memory_limit_check_only", 1);
        php_language_config.put("run", run_config);
    }

    static {
        Map<String, Object> run_config = new HashMap<String, Object>();
        run_config.put("exe_name", "solution.js");
        run_config.put("command", "/usr/bin/node {exe_path}");
        run_config.put("seccomp_rule", "");
        run_config.put("env", defaultEnv);
        run_config.put("memory_limit_check_only", 1);
        js_language_config.put("run", run_config);
    }

    public static Map<String, Object> getConfig(String language) {
        switch (language) {
            case "c":
                return LanguageConfig.c_language_config;
            case "cpp":
                return LanguageConfig.cpp_language_config;
            case "java":
                return LanguageConfig.java_language_config;
            case "python2":
                return LanguageConfig.py2_language_config;
            case "python3":
                return LanguageConfig.py3_language_config;
            case "go":
                return LanguageConfig.go_language_config;
            case "php":
                return LanguageConfig.php_language_config;
            case "js":
                return LanguageConfig.js_language_config;
            default:
                throw new IllegalArgumentException("Unsupported language: " + language);
        }
    }
}