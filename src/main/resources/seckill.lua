local voucherId = ARGV[1]
local userId = ARGV[2]
local orderId = ARGV[3]

local stockKey = 'seckill:stock:' .. voucherId

local orderKey = 'seckill:order:' .. voucherId
-- 判断库存是否充足
if (tonumber(redis.call('get', stockKey)) <= 0) then
    return 1
end

-- 判断用户是否下单,用set集合判断
if (redis.call('sismember', orderKey, userId) == 1) then
    -- 用户已下单
    return 2
end
-- 扣减库存
redis.call('incrby', stockKey, -1)

redis.call('sadd', orderKey, userId)
-- 发送到消息队列
-- 可优化为kafka
redis.call('xadd', 'stream.orders', '*', 'userId', userId, 'voucherId', voucherId, 'id', orderId)

return 0
