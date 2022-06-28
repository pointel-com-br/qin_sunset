package br.net.pin.qin_server.work;

import br.net.pin.qin_server.data.Issued;
import br.net.pin.qin_server.swap.AskIssued;

public class OrdersUtils {
  public static String askIssued(Issued issued, AskIssued question) throws Exception {
    var results = new StringBuilder();
    if (question.askCreatedAt != null && question.askCreatedAt) {
      results.append("Created At:\n");
      results.append(String.valueOf(issued.getCreatedAt()));
    }
    if (question.askResultCoded != null && question.askResultCoded) {
      results.append("Result Coded:\n");
      results.append(String.valueOf(issued.getResultCoded()));
    }
    if (question.askIsDone != null && question.askIsDone) {
      results.append("Is Done:\n");
      results.append(String.valueOf(issued.isDone()));
    }
    if (question.askFinishedAt != null && question.askFinishedAt) {
      results.append("Finished At:\n");
      results.append(String.valueOf(issued.getFinishedAt()));
    }
    if (question.askResultLines != null && question.askResultLines) {
      results.append("Result Lines:\n");
      if (question.askResultLinesFrom != null) {
        results.append(issued.getLinesFrom(question.askResultLinesFrom));
      } else {
        results.append(issued.getLines());
      }
    }
    return results.toString();
  }
}
