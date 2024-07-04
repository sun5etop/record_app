// SPDX-License-Identifier: 3.0
pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;

contract recordContract {

    // 我是一只快乐的小青蛙🐸🐸
    // LaberInfo+Person -> 唱片
    struct Record {
        uint id;
        uint recordType;
        string publishTime;     /* 发行时间 */
        // address owner;
    }

    // 公司 -> 账号
    struct Account {
        address accountAddress;
        string ownerName;
        string accountId;
    }

    // 我是一只快乐的小牛马🐂🐎
    // 劳动局 -> 唱片局


    // 社保局 -> 交易局
    struct TransactionPlatform {
        address transactionAddr;
        uint rate;              // 抽成比例
    }

    // 转移申请 -> 交易记录
    struct Transaction {
        uint recordId;            /* 唱片id */
        address fromUser;
        address toUser;
//        uint status;        /* 审批状态 -> 交易状态 0是保存未提交 1是已提交 2是已经转出 3转入已接收*/
    }

    // 缴费记录 -> 交易记录
//    struct PaymentRecord{
//        uint id;                // 唱片Id
//        address accountAddress; // 账户地址
//        uint Rate;              // 抽成比率
//        uint totalPayments;     // 总缴纳
//        uint paymentDate;       //缴费所属时间
//    }

    // 劳动局角色 -> 掉落唱片
    struct RecordOffice {
        address laodRoslAddr;
        // string city;
    }

    struct RecordOnSold {
        uint recordId;
        address seller;
        address buyer;
        uint status;    // 状态： 0在售 1已售 3下架
    }

    address owner;
    address security;
    uint recordIndex;
    constructor(){
        owner = msg.sender;
        security = msg.sender;
        recordIndex = 0;
    }

    // ---------------------------------------6528----------------------------------
    mapping (uint => Record) RecordById;
    mapping (address => uint[]) AllRecordId;
    // 1. 添加唱片
    function addRecord(uint _id, uint _recordType, string _publishTime) public {
        require(msg.sender == security);
        RecordById[_id] = Record(_id, _recordType, _publishTime);
        AllRecordId[msg.sender].push(_id);
    }

    // 2. 添加唱片们
    function addRecords(uint num, uint _recordType, string _publishTime) public returns (uint[] memory) {
        uint[] memory ret = new uint[](num);
        for(uint i = 0; i < num; i++) {
            addRecord(recordIndex, _recordType, _publishTime);
            ret[i] = recordIndex;
            recordIndex++;
        }
        return ret;
    }
    //获得当前的发行专辑编号
    function getNowrecordIndex()view public returns(uint){
        return recordIndex;
    }

    // 3. 获得所有唱片
    function getAllRecordId() view public returns (uint[] memory) {
        return AllRecordId[owner];
    }

    function getSender() view public returns(address){
        return msg.sender;
    }

    // 4. 获得唱片信息
    function getRecordInfo(uint _id) view public returns(uint, uint, string memory){
        Record memory record = RecordById[_id];
        return (record.id, record.recordType, record.publishTime);
    }

    // --------------------------------💰💰💰💰（原社保局）---------------------------------
    // mapping(address => address[]) AllAccount;       // 交易局地址 => 账号[]
    address[] AllAccount;
    mapping(address => Account) AccountByAddr;         //通过地址找账号
    mapping(address => uint[]) RecordsOnSold;          //通过用户地址查看用户在售唱片
    mapping(address => Transaction[]) Account2Transactions;  //通过账户查找交易
    function addTransactionPlatform(uint rate) {

    }

    // 2. 注册账户 (原addCompany)
    function registerAccount(address _accountAddress, string _ownerName, string _accountId) public {
        require(AccountByAddr[_accountAddress].accountAddress == address(0),"该地址已被使用");
        AllAccount.push(_accountAddress);
        AccountByAddr[_accountAddress] = Account(_accountAddress, _ownerName, _accountId);
    }

    // 3. 获得所有账号地址
    function getAllAccountAddr() public view returns (address[] memory) {
        return AllAccount;
    }

    // 我是一只快乐的小兔子🐰🐰🐰
    // 4. 获得账户信息
    function getAccountByAddr(address _accountAddress) public view returns (address, string, string) {
        Account memory account = AccountByAddr[_accountAddress];
        return (account.accountAddress, account.ownerName, account.accountId);
    }

    // 5. 发布交易（原提出申请）
    function publishTransaction(address _sellerAddr, uint _recordId) public {
        RecordsOnSold[_sellerAddr].push(_recordId);
    }

    // 6. 同意交易（购买）（原同意申请）
    function approveTransaction(address _sellerAddr, address _buyerAddr, uint _recordId) {
        // RecordsOnSold[_sellerAddr].pop(_recordId);
        // 从用户资产移除卖掉的唱片
        uint length = RecordsOwnedByAccount[_sellerAddr].length;
        uint[] memory result = new uint[](length);
        uint counter = 0;

        for(uint i = 0; i < length; i++) {
            if(RecordsOwnedByAccount[_sellerAddr][i] != _recordId) {
                result[counter] = RecordsOwnedByAccount[_sellerAddr][i];
                counter++;
            }
        }
        uint[] memory finalResult = new uint[](counter);
        for(uint j=0; j<counter;j++) {
            finalResult[j] = result[j];
        }

        RecordsOwnedByAccount[_sellerAddr] = finalResult;
        RecordsOwnedByAccount[_buyerAddr].push(_recordId);

        // 用户交易记录上链
        Account2Transactions[_sellerAddr].push(Transaction(_recordId, _sellerAddr, _buyerAddr));
        Account2Transactions[_buyerAddr].push(Transaction(_recordId, _sellerAddr, _buyerAddr));

        // 移除商品从在售列表
        recordOfTransaction(_sellerAddr, _buyerAddr, _recordId);
    }

    function recordOfTransaction(address _sellerAddr, address _buyerAddr, uint _recordId) {
        // 移除商品从在售列表
        uint length1 = RecordsOnSold[_sellerAddr].length;
        uint[] memory result_sold = new uint[](length1);
        uint counter1 = 0;

        for(uint i = 0; i < length1; i++) {
            if(RecordsOnSold[_sellerAddr][i] != _recordId) {
                result_sold[counter1] = RecordsOnSold[_sellerAddr][i];
                counter1++;
            }
        }
        uint[] memory finalResult_sold = new uint[](counter1);
        for(uint j=0; j<counter1;j++) {
            finalResult_sold[j] = result_sold[j];
        }

        RecordsOnSold[_sellerAddr] = finalResult_sold;
    }



    function getTransactionByAccount(address _accountAddr) public view returns (Transaction[]) {
        return Account2Transactions[_accountAddr];
    }

    // ------------------------------- ☔️☔️☔️☔️（原苦力局）------------------------------------
    mapping(address => uint[]) RecordsOwnedByAccount;     // 账户地址 => 账户下的唱片们
    //mapping(address => uint[]) RecordsOwnedByAccount;

    function addRecordToAccount(address _accountAddr, uint _recordId) public {
        //Record memory record = RecordById(_recordId);
        RecordsOwnedByAccount[_accountAddr].push(_recordId);
    }

    function getRecordsByAccount(address _accountAddr) public view returns (uint[]) {
        return RecordsOwnedByAccount[_accountAddr];
    }
}