package br.net.pin.qin_sunset.work;

import br.net.pin.qin_sunset.core.Authed;
import br.net.pin.qin_sunset.core.Issued;
import br.net.pin.qin_sunset.core.Way;
import br.net.pin.qin_sunset.swap.AskIssued;

public class OrdersUtils {
  public static String askParams(Way way, Authed authed, String name) {
    String result = null;
    if (authed != null) {
      result = authed.getParam(name);
    }
    if (result == null) {
      result = way.air.setup.params.get(name);
    }
    if (result == null) {
      result = "";
    }
    return result;
  }

  public static String askIssued(Issued issued, AskIssued question) throws Exception {
    var results = new StringBuilder();
    if (question.askCreatedAt != null && question.askCreatedAt) {
      results.append("Created At:");
      results.append("\n");
      results.append(String.valueOf(issued.getCreatedAt()));
      results.append("\n");
    }
    if (question.askResultCoded != null && question.askResultCoded) {
      results.append("Result Coded:");
      results.append("\n");
      results.append(String.valueOf(issued.getResultCoded()));
      results.append("\n");
    }
    if (question.askIsDone != null && question.askIsDone) {
      results.append("Is Done:");
      results.append("\n");
      results.append(String.valueOf(issued.isDone()));
      results.append("\n");
    }
    if (question.askFinishedAt != null && question.askFinishedAt) {
      results.append("Finished At:");
      results.append("\n");
      results.append(String.valueOf(issued.getFinishedAt()));
      results.append("\n");
    }
    if (question.askResultLines != null && question.askResultLines) {
      results.append("Result Lines:");
      results.append("\n");
      if (question.askResultLinesFrom != null) {
        results.append(issued.getLinesFrom(question.askResultLinesFrom));
      } else {
        results.append(issued.getLines());
      }
      results.append("\n");
    }
    return results.toString();
  }
}
