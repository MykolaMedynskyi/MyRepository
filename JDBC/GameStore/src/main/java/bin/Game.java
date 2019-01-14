package bin;

public class Game {

    private String name;
    private String dev;
    private String rate;
    private String price;
    private String id;

    public Game(String name, String dev, String rate, String price) {
        this.name = name;
        this.dev = dev;
        this.rate = rate;
        this.price = price;
    }

    public Game(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDev() {
        return dev;
    }

    public void setDev(String dev) {
        this.dev = dev;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
