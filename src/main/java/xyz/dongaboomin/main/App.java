package xyz.dongaboomin.main;

import org.sql2o.Sql2o;
import spark.ModelAndView;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import xyz.dongaboomin.attend.controller.AttendController;
import xyz.dongaboomin.attend.dto.AttendDTO;
import xyz.dongaboomin.etc.db.JedisUse;
import xyz.dongaboomin.etc.env.Env;
import xyz.dongaboomin.fcm.controller.FcmController;
import xyz.dongaboomin.greet.controller.GreetController;
import xyz.dongaboomin.etc.json.JsonTransformer;
import xyz.dongaboomin.manage.controller.ManageController;
import xyz.dongaboomin.notice.controller.NoticeController;
import xyz.dongaboomin.user.BCrypt;
import xyz.dongaboomin.user.controller.UserController;
import xyz.dongaboomin.user.dto.UserDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;


public class App {

    private static String render(Map<String, Object> model, String templatePath) {
        return new ThymeleafTemplateEngine().render(new ModelAndView(model, templatePath));
    }

    public static void main(String[] args) {
        Env env = new Env();

        String app_mode = env.getENV().getOrDefault("APP_MODE","production");

        if (app_mode.equals("debug")){
            enableDebugScreen();
        }

        staticFiles.location("/public");
        staticFiles.expireTime(600L);

        Sql2o sql2o = new Sql2o(env.getENV().get("MYSQL.HOST"), "boo_npe", "ekfqlc152!");

        JedisUse use = new JedisUse();

        GreetController greet = new GreetController(use);
        greet.setScheduler();

        port(8000);

        after((request, response) -> {
            response.header("Content-Encoding", "gzip");
        });

        after("/*/api/*",(request, response) -> response.type("application/json;charset=utf-8"));

        before("/",(request, response) -> {
            UserDTO user = request.session().attribute("user");
            if (user!=null){
                response.redirect("/selectPage");
            }
        });
        get("/",(request, response) -> {
            Map<String,Object> map = new HashMap<>();
            return render(map,"index");
        });

        path("/user", () -> {
            UserController userController = new UserController(sql2o);
            post("/reg",(request, response) -> {
                String email = request.queryParams("reg_email");
                String pw = request.queryParams("reg_pw");
                return userController.reg(email,pw);
            }, new JsonTransformer());

            post("/login",(request, response) -> {
                String email = request.queryParams("login_email");
                String pw = request.queryParams("login_pw");
                UserDTO user =  userController.login(email,pw);
                String hashed = user.getPassword();
                boolean isCorrect = BCrypt.checkpw(pw,hashed);
                if (isCorrect){
                    request.session().attribute("user",user);
                    Map<String,Object> map = new HashMap<>();
                    map.put("title","페이지선택");
                    response.redirect("/selectPage");
                    return null;
                } else {
                    return "로그인실패";
                }
            });
        });

        path("/manage", ()->{
            ManageController manageController = new ManageController(sql2o);
            get("/list", (request, response) -> {
                Map<String, Object> push = new HashMap<>();
                push.put("title","list");
                UserDTO user = request.session().attribute("user");
                push.put("user",user);
                push.put("count",(int)Math.ceil(manageController.countTable()/5.0));
                return render(push, "userList");
            });
            get("/api/:page", (request, response) -> {
                String page = request.params(":page");
                int limit = 10;
                int offset = (10*Integer.parseInt(page))-10;
                UserDTO user = request.session().attribute("user");
                int circle_id = user.getCircle_id();
                return  manageController.showUserManageList(limit, offset, circle_id);
            }, new JsonTransformer());
        });

        get("/selectPage",(request, response) -> {
            Map<String,Object> map = new HashMap<>();
            map.put("title","페이지선택");
            return render(map,"selectPage");
        });

        path("/letter", ()->{
            get("/write",(request, response) -> {
                Map<String,Object> map = new HashMap<>();
                map.put("title","쪽지보내기");
                return render(map,"letter");
            });
            post("/write",(request, response) -> {
                NoticeController notice = new NoticeController(sql2o);
                String title = request.queryParams("title");
                String contents = request.queryParams("contents");
                return notice.writeNotice(title,contents);
            }, new JsonTransformer());

        });


        path("/attend",()->{
            AttendController attendController = new AttendController(sql2o);
            get("/list", (request, response) -> {

                Map<String, Object> push = new HashMap<>();
                UserDTO user = request.session().attribute("user");
                int admin_id = user.getId();
                System.out.println(admin_id);
                push.put("title","attendLetter");
                push.put("count",(int)Math.ceil(attendController.countLetterTable(admin_id)/5.0));
                return render(push, "attendList");
            });
            get("/api/:page", (request, response) -> {
                String page = request.params(":page");
                int limit = 10;
                int offset = (10*Integer.parseInt(page))-10;
                UserDTO user = request.session().attribute("user");
                int admin_id = user.getId();
                return attendController.showAttendLetterList(limit, offset, admin_id);
            }, new JsonTransformer());
            get("/detail", (request, response) -> {
                int id = Integer.parseInt(request.queryParams("id"));
                Map<String, Object> push = new HashMap<>();
                int limit = 10;
                int offset = 0;
                List<AttendDTO> lists = attendController.showAttendList(limit, offset, id);
                Map<Integer, Integer> countMap =  attendController.countAttendPerson(id);
                push.put("lists", lists);
                push.put("listsCount", lists.size());
                push.put("countMap", countMap);
                return render(push, "attendDetail");
            });
        });

        path("/send", ()->{
            FcmController fcmController = new FcmController(sql2o);
            get("/list", (request, response) -> {
                Map<String, Object> push = new HashMap<>();
                UserDTO user = request.session().attribute("user");
                int admin_id = user.getId();
                push.put("title", "send");
                push.put("noticeCount",(int)Math.ceil(fcmController.countNNTable(admin_id)/5.0));
                push.put("circleCount",(int)Math.ceil(fcmController.countCNTable(admin_id)/5.0));
                return render(push, "send");
            });
            get("/api/normal/:page",(request, response) -> {
                String page = request.params(":page");
                int limit = 10;
                int offset = (10*Integer.parseInt(page))-10;
                UserDTO user = request.session().attribute("user");
                return  fcmController.showNormalNotice(limit, offset, user.getId());
            }, new JsonTransformer());
            get("/api/circle/:page",(request, response) -> {
                String page = request.params(":page");
                int limit = 10;
                int offset = (10*Integer.parseInt(page))-10;
                UserDTO user = request.session().attribute("user");
                return  fcmController.showCircleNotice(limit, offset, user.getId());
            }, new JsonTransformer());
        });


        after("/api/*",(request, response) -> response.type("application/json;charset=utf-8"));
        path("/api", () -> {
            post("/greet",(request, response) -> {
                //HomeText에 입력
                String text = request.queryParams("text");
                boolean isSet = greet.setText(text);
                HashMap<String,Integer> result = new HashMap<>();
                if (isSet){
                    result.put("result_code",200);
                } else {
                    result.put("result_code",500);
                }
                return result;
            }, new JsonTransformer());

            get("/greet",(request, response) -> {
                Map<String, Object> map = new HashMap<>();
                map.put("result_code",200);
                map.put("result_body",greet.getGreetMessage());
                return map;
            }, new JsonTransformer());

            get("/notice",(request, response) -> {
                NoticeController notice = new NoticeController(sql2o);
                Map<String, Object> map = new HashMap<>();
                map.put("result_code",200);
                map.put("result_body",notice.showNoticeList());
                return map;
            }, new JsonTransformer());
        });

    }

}
