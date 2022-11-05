package net.floodlightcontroller.MACTracker;

import net.floodlightcontroller.restserver.RestletRoutable;
import net.floodlightcontroller.routing.Route;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class MACTrackerRoutable implements RestletRoutable {

    @Override
    public Restlet getRestlet(Context context) {
        Router router = new Router(context);
        router.attach("/history/json",MACTrackerResource.class);
        return  router;
    }

    @Override
    public String basePath() {
        return "/wm/mactracker";
    }
}
