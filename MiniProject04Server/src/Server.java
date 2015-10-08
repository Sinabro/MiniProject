import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;
import io.vertx.ext.web.Router;

import javax.management.NotificationBroadcaster;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jiyoungpark on 15. 10. 6..
 */
public class Server extends AbstractVerticle {

    static private int count;
    private NetServer server;

    List<ServerWebSocket> websockets;
    Vertx vertx;

    // Convenience method so you can run it in your IDE
    public static void main(String[] args) {
        Server server = new Server();
        server.init();
    }

    public Server() {
        websockets = new ArrayList<>();
        vertx = Vertx.vertx();
        count = 0;
    }

    public void init() {
        vertx.deployVerticle(this);
    }

    public void start() {
        vertx.createHttpServer().websocketHandler(new Handler<ServerWebSocket>() {
            public void handle(final ServerWebSocket ws) {
                if (ws.path().equals("/myapp")) {
                    ws.handler(new Handler<Buffer>() {
                        public void handle(Buffer data) {
                            int i = 0;
                            websockets.add(count, ws);
                            count++;
                            while(i < websockets.size()) {
                                websockets.get(i).writeFinalTextFrame(data.toString());
                                System.out.println("sessions : " + ws.headers());
                                System.out.println("sessions : " + websockets.get(i));
                                i++;
                            }
                        }
                    });
                } else {
                    ws.reject();
                }
            }
        }).requestHandler(new Handler<HttpServerRequest>() {
            public void handle(HttpServerRequest req) {
                if (req.path().equals("/"))
                    req.response().sendFile("ws.html"); // Serve the html
            }
        }).listen(8080);
    }
}