package com.balance.mapper.user;

import com.balance.architecture.dto.Pagination;
import com.balance.entity.user.BankWithdraw;

import java.util.List;

/**
 * 银行卡提现
 */
public interface BankWithdrawMapper {

    List<BankWithdraw> selectByPage(Pagination<BankWithdraw> pagination);

    int updateState (BankWithdraw bankWithdraw);
}
