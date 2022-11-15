package br.net.pin.qin_sunset.core;

import java.io.FileReader;
import java.util.HashMap;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

public class GizMap extends HashMap<String, Script> {

  private final Way way;

  public GizMap(Way way) {
    this.way = way;
  }

  public Script getScript(String exec) throws Exception {
    if (!this.way.air.setup.serverDebug && this.containsKey(exec)) {
      return this.get(exec);
    }
    var binding = new Binding();
    var giz = new Giz(this.way);
    binding.setVariable("giz", giz);
    var shell = new GroovyShell(binding);
    var script = shell.parse(new FileReader(exec));
    this.put(exec, script);
    return script;
  }

}
