# btree-java
## 과제 설명
본 과제는 Java Collection Framework의 NavigableSet<Integer>과 호환되는 3-way B Tree인 MyThreeWayBTree와 Set<Integer>과 호환되는 해시 기반 MyHashSet을 설계하고 구현하는 것입니다.

#### Assignment 4
과제 4는 0부터 29까지의 정수를 순서대로 MyHashSet에 추가했을 때, 제시된 출력과 일치하는지 확인하는 과제입니다.

#### Assignment 5
과제 5는 Set<Integer> 인터페이스와 호환되며, 3-way B-Tree를 나타내는 MyThreeWayBTree 클래스를 설계하고 구현하는 것입니다.

**주의사항** <br>
MyThreeWayBTree 클래스는 3-way B-Tree를 구현해야 합니다.

## 구현 상세
과제를 수행하기 위해 다음과 같은 접근 방식을 사용했습니다:

#### Assignment 4:

MyHashSet 클래스를 설계하고 구현했습니다. 이 클래스는 NavigableSet<Integer> 인터페이스와 호환됩니다.
0부터 29까지의 정수를 순서대로 MyHashSet에 추가하는 메서드를 구현했습니다.
제시된 출력과 일치하는지 확인하기 위해 필요한 메서드를 구현했습니다.
#### Assignment 5:

MyThreeWayBTree 클래스를 설계하고 구현했습니다. 이 클래스는 Set<Integer> 인터페이스와 호환됩니다.
3-way B-Tree를 구현하기 위해 필요한 메서드를 구현했습니다.
구현 중에 마주한 주요한 설계 결정 사항이나 어려움을 강조했습니다.

## 테스트
구현한 클래스들을 테스트하기 위해 다음과 같은 방법을 사용했습니다:

제공된 UnitTest4.java와 UnitTest5.java를 사용하여 단위 테스트를 수행했습니다.
결과
아래는 각 과제의 테스트 결과와 기대 출력과의 비교입니다:

#### Assignment 4:
[1] true
[2] true
[3] false
[4] false
[5] 30
[6] 1,4,7,10,13,16,19,22,25,28,0,3,6,9,12,15,18,21,24,27,2,5,8,11,14,17,20,23,26,29
#### Assignment 5:
[1] true
[2] true
[3] true
[4] true
  
