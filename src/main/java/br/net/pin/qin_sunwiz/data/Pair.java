package br.net.pin.qin_sunwiz.data;

public class Pair<T, S> {
  public T head;
  public S tail;

  public Pair() {
  }

  public Pair(T head) {
    this.head = head;
  }

  public Pair(T head, S tail) {
    this.head = head;
    this.tail = tail;
  }
}
