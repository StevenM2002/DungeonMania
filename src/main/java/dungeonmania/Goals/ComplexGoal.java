package dungeonmania.Goals;

public abstract class ComplexGoal implements Goal {
    private Goal left;
    private Goal right;
    public ComplexGoal(Goal left, Goal right) {
        this.left = left;
        this.right = right;
    }
    
    public Goal getLeft() {
        return left;
    }

    public Goal getRight() {
        return right;
    }
}
