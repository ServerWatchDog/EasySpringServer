package i.watch.utils

/**
 *   SnowFlake算法
 */

class SnowFlakeUtils(
    private val dataCenterId: Long, // 数据中心
    private val machineId: Long // 机器标识
) {
    private var sequence = 0L // 序列号
    private var lastTimestamp = -1L // 上一次时间戳

    private val nextMill: Long
        get() {
            var mill = newTimestamp
            while (mill <= lastTimestamp) {
                mill = newTimestamp
            }
            return mill
        }

    private val newTimestamp: Long
        get() = System.currentTimeMillis()

    init {
        if (dataCenterId > MAX_DATA_CENTER_NUM || dataCenterId < 0) {
            throw IllegalArgumentException("dataCenterId can't be greater than MAX_DATA_CENTER_NUM or less than 0")
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0")
        }
    }

    /**
     * 产生下一个ID
     */
    @Synchronized
    fun nextId(): Long {
        var currentStamp = newTimestamp
        if (currentStamp < lastTimestamp) {
            throw RuntimeException("Clock moved backwards.  Refusing to generate id")
        }

        if (currentStamp == lastTimestamp) {
            // 相同毫秒内，序列号自增
            sequence = sequence + 1 and MAX_SEQUENCE
            // 同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currentStamp = nextMill
            }
        } else {
            // 不同毫秒内，序列号置为0
            sequence = 0L
        }

        lastTimestamp = currentStamp

        return (
            currentStamp - START_STAMP shl TIMESTAMP_LEFT // 时间戳部分
                or (
                    dataCenterId shl DATA_CENTER_LEFT // 数据中心部分
                    )
                or (
                    machineId shl MACHINE_LEFT // 机器标识部分
                    )
                or sequence
            ) // 序列号部分
    }

    companion object {
        /**
         * 起始的时间戳
         */
        private const val START_STAMP = 1480166465631L

        /**
         * 每一部分占用的位数
         */
        private const val SEQUENCE_BIT: Int = 12 // 序列号占用的位数
        private const val MACHINE_BIT: Int = 5 // 机器标识占用的位数
        private const val DATA_CENTER_BIT: Int = 5 // 数据中心占用的位数

        /**
         * 每一部分的最大值
         */
        private const val MAX_DATA_CENTER_NUM = (-1L shl DATA_CENTER_BIT).inv()
        private const val MAX_MACHINE_NUM = (-1L shl MACHINE_BIT).inv()
        private const val MAX_SEQUENCE = (-1L shl SEQUENCE_BIT).inv()

        /**
         * 每一部分向左的位移
         */
        private const val MACHINE_LEFT = SEQUENCE_BIT
        private const val DATA_CENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT
        private const val TIMESTAMP_LEFT = DATA_CENTER_LEFT + DATA_CENTER_BIT
    }
}
