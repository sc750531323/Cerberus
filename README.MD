#Cerberus
Cerberus is a high performance gateway  
基于netty构建，目标是完成一个工业生产级别的网关  
## disruptor高性能之道
1.RingBuffer数据结构，与运算  
2.预热填充数据，数据的预加载，即预热的思想  
3.不同加锁策略，等待策略  
4.填充缓冲行，消除伪共享

