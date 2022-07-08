public class TestSpiderInitialMovement {
    @Test
    public void SpiderInitialMovement() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse res = game.newGame("d_Test_SpiderInitial", "c_Test_SpiderConfig");
        Position pos = getEntities(res, "spider").get(0).getPosition();
        List<Position> movementTrajectory = new ArrayList<Position>();
        int x = pos.getX();
        int y = pos.getY();
        int nextPositionElement = 0;
        movementTrajectory.add(new Position(x, y + 1));
        movementTrajectory.add(new Position(x - 1, y + 1));

        // Assert Circular Movement of Spider
        for (int i = 0; i <= 1; ++i) {
            res = game.tick(Direction.UP);
            assertEquals(movementTrajectory.get(nextPositionElement), getEntities(res, "spider").get(0).getPosition());

            nextPositionElement++;
        }

    }
}
