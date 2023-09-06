package br.net.pin.qin_sunset.work;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import br.net.pin.qin_sunset.core.Authed;
import br.net.pin.qin_sunset.core.Way;
import jakarta.servlet.http.HttpServletResponse;

public class OrdersAPP {
    public static void send(File file, HttpServletResponse resp) throws IOException {
        resp.setContentType(Utils.getMimeType(file.getName()));
        resp.setContentLength((int) file.length());
        try (var input = new FileInputStream(file)) {
            IOUtils.copy(input, resp.getOutputStream());
        }
    }

    public static String list(Way way, Authed forAuthed) {
        var appsDir = new File(way.air.setup.serverFolder, "app");
        if (forAuthed.isMaster()) {
            return Utils.listFolders(appsDir);
        }
        var result = new StringBuilder();
        for (var access : forAuthed.getAccess()) {
            if (access.app != null) {
                if (new File(appsDir, access.app.name).exists()) {
                    result.append(access.app.name);
                    result.append("\n");
                }
            }
        }
        return result.toString();
    }
}
