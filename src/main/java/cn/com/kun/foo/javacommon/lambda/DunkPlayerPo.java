package cn.com.kun.foo.javacommon.lambda;

public class DunkPlayerPo {
    private String name;
    private Double score;

    public DunkPlayerPo(String name, Double score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "UserPo{" +
                "name='" + name + '\'' +
                ", score=" + score +
                '}';
    }
}