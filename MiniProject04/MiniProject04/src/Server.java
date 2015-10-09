import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebsocketVersion;
import io.vertx.core.net.NetServer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by jiyoungpark on 15. 10. 6..
 */
public class Server extends AbstractVerticle {

    static private int count;
    String message;

    ConcurrentMap<Integer, ServerWebSocket> sockets;
    List<ServerWebSocket> websockets;
    ArrayList<String> messages;
    Vertx vertx;

    // Convenience method so you can run it in your IDE
    public static void main(String[] args) {
        Server server = new Server();
        server.init();
    }

    public Server() {
        message = null;
        websockets = new ArrayList<ServerWebSocket>();
        sockets = new ConcurrentHashMap<>();
        messages = new ArrayList<>();
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
                            if (!sockets.containsValue(ws)) {
                                sockets.put(count, ws);
                                for (int num = messages.size() - 1; num != messages.size() - 10; num--) {
                                    try {
                                        if (count == 0)
                                            break;
                                        sockets.get(count).writeFinalTextFrame(messages.get(num));
                                    } catch (ArrayIndexOutOfBoundsException e) {}
                                    }
                                    count++;
                                }

                                System.out.println("sessions : " + data);

                                message = "<tr><td style=\"text-align:left; height=auto;\">" + ws.binaryHandlerID() + " >> " + data.toString() + "</td></tr>";
                                messages.add(message);

                                for (int i = 0; i < sockets.size(); i++) {
                                    try {
                                        if (sockets.get(i) == ws)
                                            sockets.get(i).writeFinalTextFrame("<tr><td style=\"text-align:right; height=auto;\">" + data.toString() + "<//td></tr>");
                                        else
                                            sockets.get(i).writeFinalTextFrame(message);
                                    } catch (IllegalStateException e) {
                                    }
                                }
                            }
                               }

                    );
                } else {
                    ws.reject();
                }
            }
                                                  }

        ).

                requestHandler(new Handler<HttpServerRequest>() {
                                   public void handle(HttpServerRequest req) {
                                       if (req.path().equals("/"))
                                       req.response().sendFile("ws.html"); // Serve the html
                               }
                           }

            ).

            listen(8080);
        }
    }