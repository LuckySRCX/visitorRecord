package net.jiaobaowang.visitor.visitor_interface;

/**
 * 识别条码/二维码
 * Created by ShangLinMo on 2018/1/20.
 */

public interface OnGetQRCodeResult {
    /**
     * 识别条码/二维码
     *
     * @param code   0，失败；1，成功
     * @param msg    失败信息
     * @param qrCode 条码/二维码
     */
    void getQRCodeResult(int code, String msg, String qrCode);
}
