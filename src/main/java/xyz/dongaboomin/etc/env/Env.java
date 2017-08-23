package xyz.dongaboomin.etc.env;

import com.github.shyiko.dotenv.DotEnv;

import java.util.Map;

public class Env {
    public Map<String, String> getENV(){
        Map<String, String> dotEnv = DotEnv.load();
        return dotEnv;
    }
}
