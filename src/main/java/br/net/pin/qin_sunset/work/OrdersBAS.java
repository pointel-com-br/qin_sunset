package br.net.pin.qin_sunset.work;

import br.net.pin.qin_sunset.core.Authed;
import br.net.pin.qin_sunset.core.Way;

public class OrdersBAS {
    public static String list(Way way, Authed forAuthed) {
        var result = new StringBuilder();
        for (var base : way.air.bases) {
            var name = base.getName();
            if (forAuthed.allowBAS(name, false)) {
                result.append(name).append("\n");
            }
        }
        return result.toString();
    }
}
