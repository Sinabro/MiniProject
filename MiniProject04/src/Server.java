import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VoidHandler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;

/**
 * Created by jiyoungpark on 15. 10. 6..
 */
public class Server extends AbstractVerticle {
    Vertx vertx;

    // Convenience method so you can run it in your IDE
    public static void main(String[] args) {
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

        NetServer netServer = vertx.createNetServer();
        netServer.connectHandler(new Handler<NetSocket>() {
            @Override
            public void handle(NetSocket netSocket) {
                netSocket.handler(new Handler<Buffer>() {
                    @Override
                    public void handle(Buffer buffer) {
                        netSocket.write("hello world");
                        System.out.println("receive data : " + buffer.toString());
                    }
                });
                netSocket.closeHandler(new VoidHandler() {
                    @Override
                    protected void handle() {

                    }
                });
            }
        });

        netServer.listen(8080, "localhost");
    }
}