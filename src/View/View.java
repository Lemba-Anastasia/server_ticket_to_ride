package View;

public class View {
    private Server server;
    public View(){
        addComandInArea("The server started");
        new Thread(new Server(View.this)).start();
    }

    public void addComandInArea(String string){
        System.out.println(string);
    }
}
