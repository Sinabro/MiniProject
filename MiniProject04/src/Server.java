import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

/**
 * Created by jiyoungpark on 15. 10. 6..
 */
public class Server extends AbstractVerticle {

    Vertx vertx;
    // Convenience method so you can run it in your IDE
    public static void main(String[] args) {
//        Runner.runExample(Server.class);

        Server server = new Server();
        server.init();
    }

    public Server() {
        vertx = Vertx.vertx();
    }

    public void init() {
        vertx.deployVerticle(this);
    }

    @Override
    public void start() throws Exception {
        vertx.createHttpServer().requestHandler(req -> {
            req.response().putHeader("content-type", "text/html").end("<html><body><h1>Hello from vert.x!</h1></body></html>");
        }).listen(8080);
    }
}