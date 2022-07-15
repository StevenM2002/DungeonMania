package dungeonmania;

import dungeonmania.StaticEntities.Portal;
import java.util.stream.Stream;
import java.util.List;

public class PortalMatcher {
    
    /**
     * used after creating all entities
     * Sets portal field otherPortal to the other Portal of the corresponding 
     * colour 
     * @param allEntities
     */
    public static void configurePortals(List<Entity> allEntities) {
        Stream<Portal> portalStream = allEntities.stream()
            .filter(x->(x instanceof Portal))
            .map(x->(Portal) x);
        // Assumes that there are always two portals of matching colour
        while (portalStream.anyMatch(x-> x.getOtherPortal() == null)) {
            Portal firstPortal = portalStream.findFirst().get();
            firstPortal.setOtherPortal(portalStream
                .filter(x->x.getId() != firstPortal.getId() && x.getColour() == firstPortal.getColour())
                .findFirst().get()
            );
            firstPortal.getOtherPortal().setOtherPortal(firstPortal);
        }
    }
}
