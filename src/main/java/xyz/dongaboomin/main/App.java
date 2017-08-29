package xyz.dongaboomin.main;

import org.sql2o.Sql2o;
import spark.ModelAndView;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import xyz.dongaboomin.attend.controller.AttendController;
import xyz.dongaboomin.attend.dto.AttendDTO;
import xyz.dongaboomin.attend.dto.AttendLetterDTO;
import xyz.dongaboomin.etc.db.JedisUse;
import xyz.dongaboomin.etc.env.Env;
import xyz.dongaboomin.fcm.controller.FcmController;
import xyz.dongaboomin.fcm.dto.CircleFcmDTO;
import xyz.dongaboomin.fcm.dto.NormalFcmDTO;
import xyz.dongaboomin.greet.controller.GreetController;
import xyz.dongaboomin.etc.json.JsonTransformer;
import xyz.dongaboomin.login.LoginController;
import xyz.dongaboomin.manage.controller.ManageController;
import xyz.dongaboomin.manage.dto.PartManageDTO;
import xyz.dongaboomin.notice.controller.NoticeController;
import xyz.dongaboomin.user.BCrypt;
import xyz.dongaboomin.user.controller.UserController;
import xyz.dongaboomin.user.dto.UserDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

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
                LoginController loginController = new LoginController();

                String email = request.queryParams("login_email");
                String pw = request.queryParams("login_pw");
                UserDTO user =  userController.login(email,pw);
                String hashed = user.getPassword();

                BlockingQueue<String> result = loginController.login(email, pw);
                Map<String, Object> map = new HashMap<>();
                String token = result.take();
                request.session().attribute("token", token);
                System.out.println("사실 세션에 저장된 토큰은 :: "+request.session().attribute("token"));
                map.put("result_code",200);
                map.put("result_body",token);

                boolean isCorrect = BCrypt.checkpw(pw,hashed);
                if (isCorrect){
                    request.session().attribute("user",user);
//                        Map<String,Object> map = new HashMap<>();
                    map.put("title","페이지선택");
                    response.redirect("/selectPage");
                    return map;
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
                push.put("count",manageController.countTable(user.getCircle_id()));
                return render(push, "userList");
            });
            get("/api/user", (request, response) -> {
                String page = request.queryParams("pageNumber");
                int limit = 10;
                int offset = (10*Integer.parseInt(page))-10;
                UserDTO user = request.session().attribute("user");
                Map<String, Object> push = new HashMap<>();
                List<PartManageDTO> items = manageController.showUserManageList(limit, offset, user.getCircle_id());
                push.put("result_code",200);
                push.put("items",items);
                return push;
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
                push.put("title","attendLetter");
                push.put("count",attendController.countLetterTable(admin_id));
                return render(push, "attendList");
            });
            get("/api/circle", (request, response) -> {
                String page = request.queryParams("pageNumber");
                int limit = 10;
                int offset = (10*Integer.parseInt(page))-10;
                UserDTO user = request.session().attribute("user");
                Map<String, Object> push = new HashMap<>();
                List<AttendLetterDTO> items = attendController.showAttendLetterList(limit, offset, user.getId());
                push.put("result_code",200);
                push.put("items",items);
                return push;
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
                push.put("noticeCount",fcmController.countNNTable(admin_id));
                push.put("circleCount",fcmController.countCNTable(admin_id));
                return render(push, "send");
            });
            get("/api/normal",(request, response) -> {
                String page = request.queryParams("pageNumber");
                int limit = 10;
                int offset = (10*Integer.parseInt(page))-10;
                UserDTO user = request.session().attribute("user");
                Map<String, Object> push = new HashMap<>();
                List<NormalFcmDTO> items = fcmController.showNormalNotice(limit, offset, user.getId());
                push.put("result_code",200);
                push.put("items",items);
                return push;
            }, new JsonTransformer());
            get("/api/circle",(request, response) -> {
                String page = request.queryParams("pageNumber");
                int limit = 10;
                int offset = (10*Integer.parseInt(page))-10;
                UserDTO user = request.session().attribute("user");
                Map<String, Object> push = new HashMap<>();
                List<CircleFcmDTO> items = fcmController.showCircleNotice(limit, offset, user.getId());
                push.put("result_code",200);
                push.put("items",items);
                return push;
            }, new JsonTransformer());
            post("/circleNoticeFcm",(request, response) -> {
                String title = request.queryParams("title");
                String contents = request.queryParams("body");
                String body = request.queryParams("contents");
                System.out.println("title::"+title+", contents::"+contents+", body::"+body+", token::"+request.session().attribute("token"));
                BlockingQueue<String> result = fcmController.circleNoticeFcm(request.session().attribute("token"),
                        title, body, contents);
                String result_code = result.take();
                Map<String, Object> map = new HashMap<>();
                if (result_code.equals("1")){
                    map.put("result_code",200);
                }else{
                    map.put("result_code",500);
                }
                return map;
            }, new JsonTransformer());
            post("/normalNoticeFcm",(request, response) -> {
                String title = request.queryParams("title");
                String contents = request.queryParams("body");
                String body = request.queryParams("contents");
                System.out.println("title::"+title+", contents::"+contents+", body::"+body+", token::"+request.session().attribute("token"));
                BlockingQueue<String> result = fcmController.normalNoticeFcm(request.session().attribute("token"),
                        title, body, contents);
                String result_code = result.take();
                Map<String, Object> map = new HashMap<>();
                if (result_code.equals("1")){
                    map.put("result_code",200);
                }else{
                    map.put("result_code",500);
                }
                return map;
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
