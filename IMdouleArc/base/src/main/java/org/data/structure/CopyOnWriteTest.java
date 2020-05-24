package org.data.structure;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * File: CopyOnWriteArrayListTest.java
 * Author: yuzhuzhang
 * Create: 2020/3/15 10:10 PM
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020/3/15 : Create CopyOnWriteArrayListTest.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class CopyOnWriteTest {
    //Copy-On-Write，写入时复制，这个技术，准确的说应该是一种思想

    // Copy-On-Write 多读少写的场景 写入时复制（CopyOnWrite）思想  如何提高性能

    //优化策略 基本设置思路 多个线程共享一个列表】

    // 写入时复制（CopyOnWrite，简称COW）思想是计算机程序设计领域中的一种优化策略。
    // 其核心思想是，如果有多个调用者（Callers）同时要求相同的资源（如内存或者是磁盘上的数据存储），
    // 他们会共同获取相同的指针指向相同的资源，直到某个调用者视图修改资源内容时，
    // 系统才会真正复制一份专用副本（private copy）给该调用者，而其他调用者所见到的最初的资源仍然保持不变。
    // 这过程对其他的调用者都是透明的（transparently）。
    // 此做法主要的优点是如果调用者没有修改资源，就不会有副本（private copy）被创建，
    // 因此多个调用者只是读取操作时可以共享同一份资源


    //优点 写时拷贝原理 可以将读写分离 在高并发场景下对容器操作效率得到提高；
    //缺点  内存空间问题  : 容器在添加 、移除 元素时由于采用对时copy出一个新的容器替代方式。这样会使得内存空间翻一倍，典型对空间换时间原则；
    //     数据一致性问题 : CopyOnWrite容器只能保证数据的最终一致性，不能保证数据的实时一致性。所以如果你希望写入的的数据，马上能读到，请不要使用CopyOnWrite容器

    //场景：CopyOnWrite并发容器用于读多写少的并发场景。比如白名单，黑名单，商品类目的访问和更新场景

    //volatile （挥发物、易变的）：变量修饰符，只能用来修饰变量。volatile修饰的成员变量在每次被线程访问时，
    // 都强迫从共享内存中重读该成员变量的值。而且，当成员变量发生变 化时，强迫线程将变化值回写到共享内存。
    // 这样在任何时刻，两个不同的线程总是看到某个成员变量的同一个值。

    // CopyOnWriteArrayList为什么并发安全且性能比Vector好
    // 我知道Vector是增删改查方法都加了synchronized，保证同步，但是每个方法执行的时候都要去获得锁，性能就会大大下降，而CopyOnWriteArrayList 只是在增删改上加锁，但是读不加锁，在读方面的性能就好于Vector，CopyOnWriteArrayList支持读多写少的并发情况




    private void init() {
        new CopyOnWriteArrayList<String>();
        new CopyOnWriteArraySet<String>();

        //new CopyOnWriteMap();
    }
}
