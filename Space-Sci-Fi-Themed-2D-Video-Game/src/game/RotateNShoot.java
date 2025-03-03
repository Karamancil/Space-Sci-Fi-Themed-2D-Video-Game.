package game7;

public class RotateNShoot implements Controller {
    Action action = new Action();

    @Override
    public Action action() {
        action.shoot = true;
        action.turn = 1;
        return action;
    }
}
