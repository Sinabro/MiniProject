import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.ext.web.handler.sockjs.SockJSSocket;
import io.vertx.ext.web.handler.sockjs.impl.SockJSSocketBase;

import java.util.List;

/**
 * Created by jiyoungpark on 15. 10. 6..
 */
public class Server extends AbstractVerticle {

    private NetServer server;
    private List<NetSocket> sockets;

    Vertx vertx;
    Router router;


    // Convenience method so you can run it in your IDE
    public static void main(String[] args) {
        Server server = new Server();
        server.init();
    }

    public Server() {
        vertx = Vertx.vertx();
        router = Router.router(vertx);
    }

    public void init() {
        vertx.deployVerticle(this);
    }

    @Override
    public void start() throws Exception {

        HttpServer httpServer = vertx.createHttpServer();
        httpServer.requestHandler(new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest httpServerRequest) {
                String method = String.valueOf(httpServerRequest.method());
                String uri = httpServerRequest.uri();
                String path = httpServerRequest.path();
                String query = httpServerRequest.query();

                System.out.println("Receive http request : {method=" + method + ", uri=" + uri + ", path=" + path + ", query=" + query + "}");

                httpServerRequest.bodyHandler(new Handler<Buffer>() {
                    @Override
                    public void handle(Buffer buffer) {
                        System.out.println("Receive data : " + buffer.toString());
                    }
                });
                httpServerRequest.response().setStatusCode(200).end("OK");
            }
        });


        SockJSHandler sockJSHandler = SockJSHandler.create(vertx);

        //router.route("/mySockJS").handler(sockJSHandler);
        //JsonObject config = new JsonObject();
        //config.put("prefix", "/mySockJS");


        sockJSHandler.socketHandler(new Handler<SockJSSocket>() {
            @Override
            public void handle(SockJSSocket sockJSSocket) {
                sockJSSocket.handler(new Handler<Buffer>() {
                    @Override
                    public void handle(Buffer buffer) {
                        System.out.println("receive data : " + buffer.toString());
                        sockJSSocket.write(buffer);
                    }
                });
                sockJSSocket.exceptionHandler(new Handler<Throwable>() {
                    @Override
                    public void handle(Throwable throwable) {
                        System.out.println("unexpected exception: " + throwable);
                    }
                });
            }
        });

        httpServer.listen(8080, new Handler<AsyncResult<HttpServer>>() {
            @Override
            public void handle(AsyncResult<HttpServer> asyncResult) {
                System.out.println("bind result: " + asyncResult.succeeded());
            }
        });
    }
}