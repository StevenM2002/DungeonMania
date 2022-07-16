package dungeonmania;

import dungeonmania.StaticEntities.Portal;
import java.util.List;

public class PortalMatcher {
    
    /**
     * used after creating all entities
     * Sets portal field otherPortal to the other Portal of the corresponding 
     * colour 
     * @param allEntities
     */
    public static void configurePortals(List<Entity> allEntities) {
        // Assumes that there are always two portals of matching colour
        while (allEntities.stream()
            .filter(x->(x instanceof Portal))
            .map(x->(Portal) x)
            .anyMatch(x-> x.getOtherPortal() == null)
        ) {
            Portal firstPortal = allEntities.stream()
                .filter(x->(x instanceof Portal))
                .map(x->(Portal) x)
                .filter(x->x.getOtherPortal() == null)
                .findFirst().get();
            firstPortal.setOtherPortal(allEntities.stream()
                .filter(x->(x instanceof Portal))
                .map(x->(Portal) x)
                .filter(x->!x.getId().equals(firstPortal.getId()) && x.getColour().equals(firstPortal.getColour()))
                .findFirst().get()
            );
            firstPortal.getOtherPortal().setOtherPortal(firstPortal);
        }
    }
}
