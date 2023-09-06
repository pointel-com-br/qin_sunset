package br.net.pin.qin_sunwiz.flow;

import java.sql.ResultSet;
import java.util.List;
import br.net.pin.qin_sunwiz.data.Nature;
import br.net.pin.qin_sunwiz.data.Typed;
import br.net.pin.qin_sunwiz.mage.WizData;

public class CSVMaker {
    private final ResultSet results;
    private final Nature[] natures;

    public CSVMaker(ResultSet results, List<Typed> typeds) throws Exception {
        this.results = results;
        if (typeds != null) {
            this.natures = new Nature[typeds.size()];
            for (int i = 0; i < typeds.size(); i++) {
                this.natures[i] = typeds.get(i).type;
            }
        } else {
            this.natures = WizData.getNaturesFrom(results);
        }
    }

    public CSVMaker(ResultSet results, Nature[] natures) throws Exception {
        this.results = results;
        if (natures != null) {
            this.natures = natures;
        } else {
            this.natures = WizData.getNaturesFrom(results);
        }
    }

    public String[] makeLine() throws Exception {
        if (!this.results.next()) {
            return null;
        }
        var result = new String[this.natures.length];
        for (int i = 1; i <= this.natures.length; i++) {
            result[(i - 1)] = WizData.formatValue(this.natures[(i - 1)], this.results
                            .getObject(i));
        }
        return result;
    }
}
