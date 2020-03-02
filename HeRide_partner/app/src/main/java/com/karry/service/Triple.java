package com.karry.service;

class Triple<T, U, V> {

  private final T first;
  private final U second;
  private final V third;

  public Triple(T first, U second, V third) {
    this.first = first;
    this.second = second;
    this.third = third;
  }

  public T getFirst() { return first; }
  public U getSecond() { return second; }
  public V getThird() { return third; }

  /**
   * <p>Returns a String representation of this triple using the format {@code
   * ($left, $middle, $right)}.</p>
   *
   * @return a string describing this object, not null
   */
  @Override
  public String toString()
  {
    return "{" + getFirst() + ',' + getSecond() + ',' + getThird() + '}';
  }
}
