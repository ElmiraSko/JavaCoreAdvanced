package Tasks;
//Практическое задание из методички
public class Main {
    public static void main(String[] args) {

//Массив участников
        Participantable[] participant = new Participantable[8];
        participant[0] = new Person("Участник_1", 300, 1.1f);
        participant[1] = new Person("Участник_2", 460, 1.5f);
        participant[2] = new Person("Участник_3", 500, 1.7f);
        participant[3] = new Cat("Мурзик", 500, 1.3f);
        participant[4] = new Cat("Мурка", 200, 1.0f);
        participant[5] = new Robot("Робот_1", 100, 0.5f);
        participant[6] = new Robot("Робот_2", 300, 1.4f);
        participant[7] = new Robot("Робот_3", 350, 3.5f);

//Массив препятствий
        Resistuble[] difficultys = new Resistuble[6];
        difficultys[0] = new Wall(0.5f);
        difficultys[1] = new Wall(1.4f);
        difficultys[2] = new Wall(2.7f);
        difficultys[3] = new RunningTrack(220);
        difficultys[4] = new RunningTrack(300);
        difficultys[5] = new RunningTrack(550);

//Участники проходят набор препятствий
        for(Participantable p : participant) {
            for (Resistuble d : difficultys) {

                if ((d.getClass()).equals(Wall.class)){
                    if (p.jump(d.getLength())){
                        System.out.println("Отлично прыгает!");
                    }else {
                        System.out.println("Увы, ему не удалось так высоко подпрыгнуть. " + p.getName() + " покидает соревнования.");
                        break;}
                } else {
                    if (p.run(d.getLength())) {
                    System.out.println("Отлично бежит!");
                    }else {
                        System.out.println("Увы, пробег не удался. " + p.getName() + " покидает соревнования.");
                        break;
                    }
                }
            }
        }
    }
}
