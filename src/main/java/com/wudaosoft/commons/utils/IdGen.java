 /**
 *    Copyright 2009-2018 Wudao Software Studio(wudaosoft.com)
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.wudaosoft.commons.utils;

/**
 * @author Changsoul Wu
 * 
 */
public class IdGen {
	private long workerId;
	private long datacenterId;
	private long sequence = 0L;
	private long twepoch = 1511208000000L; // Thu, 04 Nov 2010 01:42:54 GMT
	private long workerIdBits = 5L; // 节点ID长度
	private long datacenterIdBits = 5L; // 数据中心ID长度
	private long maxWorkerId = -1L ^ (-1L << workerIdBits); // 最大支持机器节点数0~31，一共32个
	private long maxDatacenterId = -1L ^ (-1L << datacenterIdBits); // 最大支持数据中心节点数0~31，一共32个
	private long sequenceBits = 12L; // 序列号12位
	private long workerIdShift = sequenceBits; // 机器节点左移12位
	private long datacenterIdShift = sequenceBits + workerIdBits; // 数据中心节点左移17位
	private long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits; // 时间毫秒数左移22位
	private long sequenceMask = -1L ^ (-1L << sequenceBits); // 4095
	private long lastTimestamp = -1L;

	private static class IdGenHolder {
		private static final IdGen instance = new IdGen();
	}

	public static IdGen get() {
		return IdGenHolder.instance;
	}

	private IdGen() {
		this(0L, 0L);
	}

	public IdGen(long workerId, long datacenterId) {
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(
					String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
		}
		if (datacenterId > maxDatacenterId || datacenterId < 0) {
			throw new IllegalArgumentException(
					String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
		}
		this.workerId = workerId;
		this.datacenterId = datacenterId;
	}

	public synchronized long nextId() {
		long timestamp = timeGen(); // 获取当前毫秒数
		// 如果服务器时间有问题(时钟后退) 报错。
		if (timestamp < lastTimestamp) {
			throw new RuntimeException(String.format(
					"Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
		}
		// 如果上次生成时间和当前时间相同,在同一毫秒内
		if (lastTimestamp == timestamp) {
			// sequence自增，因为sequence只有12bit，所以和sequenceMask相与一下，去掉高位
			sequence = (sequence + 1) & sequenceMask;
			// 判断是否溢出,也就是每毫秒内超过4095，当为4096时，与sequenceMask相与，sequence就等于0
			if (sequence == 0) {
				timestamp = tilNextMillis(lastTimestamp); // 自旋等待到下一毫秒
			}
		} else {
			sequence = 0L; // 如果和上次生成时间不同,重置sequence，就是下一毫秒开始，sequence计数重新从0开始累加
		}
		lastTimestamp = timestamp;
		// 最后按照规则拼出ID。
		// 000000000000000000000000000000000000000000 00000 00000 000000000000
		// time datacenterId workerId sequence
		return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift)
				| (workerId << workerIdShift) | sequence;
	}

	protected long tilNextMillis(long lastTimestamp) {
		long timestamp = timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = timeGen();
		}
		return timestamp;
	}

	protected long timeGen() {
		return System.currentTimeMillis();
	}
	
	public static void main(String[] args) {
		System.out.println(IdGen.get().nextId());
		System.out.println(System.currentTimeMillis());
	}
}
