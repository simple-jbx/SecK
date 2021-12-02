if redis.call("get",KEYS[1]) == ARGV[1] then
    if (redis.call('exists', KEYS[2]) == 1) then
       local stock = tonumber(redis.call('get', KEYS[2]));
       if (stock > 0) then
          redis.call('incrby', KEYS[2], -1)
          return stock
       end
       return 0
    end
    return redis.call("del",KEYS[1])
else
    return 0
end

