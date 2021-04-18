package com.lzb.transaction.config;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;

import java.sql.Connection;

/**
 * @author 空青
 * @date 2020/5/25
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class PageInterceptor extends PaginationInterceptor {

    /**
     * 调整 分页溢出 使用最后一页
     */
    @Override
    protected void handlerOverflow(IPage<?> page) {
        page.setCurrent(page.getPages());
    }
}
