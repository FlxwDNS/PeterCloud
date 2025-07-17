package dev.easycloud;

public class BootstrapBoot {

    public static void main(String[] args) {
        var bootstrap = new Bootstrap();
        bootstrap.load();
        bootstrap.run();
    }
}
