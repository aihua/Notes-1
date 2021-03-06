package com.agile.payroll.test;

import com.agile.payroll.classifications.AddTimeCard;
import com.agile.payroll.domain.PayCheck;
import com.agile.payroll.transactions.AddHourlyEmployee;
import com.agile.payroll.transactions.PaydayTransaction;
import junit.framework.TestCase;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jiangrui
 * Date: 13-6-14
 * Time: 上午8:40
 * To change this template use File | Settings | File Templates.
 */
public class PaydayTransactionTest extends TestCase {
    public void testSingleHourlyEmployeeNoPayDay() {
        String id = "empTSHE000";

        AddHourlyEmployee aht = new AddHourlyEmployee(id, "Bob", "Beijing", 100.0);
        aht.execute();
        assertNotNull(aht);

        // saturday, not a pay day(saturday)
        PaydayTransaction pdt = new PaydayTransaction(new Date(113, 5, 15).getTime());
        pdt.execute();

        PayCheck pc = pdt.getPayCheck(id);
        assertNull(pc);
    }

    public void testSingleHourlyEmployeeNoTimeCard() {
        String id = "empTSHE001";

        AddHourlyEmployee aht = new AddHourlyEmployee(id, "Bob", "Beijing", 100.0);
        aht.execute();
        assertNotNull(aht);

        PaydayTransaction pdt = new PaydayTransaction(new Date(113, 5, 14).getTime());
        pdt.execute();

        PayCheck pc = pdt.getPayCheck(id);
        assertNotNull(pc);
        assertEquals(0.0, pc.getGrossPay());
        assertEquals(0.0, pc.getNetPay());
    }

    public void testSingleHourlyEmployeeWithTimeCard() {
        String id = "empTSHE002";

        AddHourlyEmployee aht = new AddHourlyEmployee(id, "Bob", "Beijing", 100.0);
        aht.execute();
        assertNotNull(aht);

        AddTimeCard tt = new AddTimeCard(new Date(113, 5, 14).getTime(), 8, id);
        tt.execute();

        PaydayTransaction pdt = new PaydayTransaction(new Date(113, 5, 14).getTime());
        pdt.execute();

        PayCheck pc = pdt.getPayCheck(id);
        assertNotNull(pc);
        assertEquals(800.0, pc.getGrossPay());
        assertEquals(800.0, pc.getNetPay());
    }

    public void testSingleHourlyEmployeeWithTimeCards() {
        String id = "empTSHE003";

        AddHourlyEmployee aht = new AddHourlyEmployee(id, "Bob", "Beijing", 100.0);
        aht.execute();
        assertNotNull(aht);

        // week day
        AddTimeCard tt1 = new AddTimeCard(
                new Date(113, 5, 10).getTime(), 2, id);
        tt1.execute();

        // week day
        AddTimeCard tt2 = new AddTimeCard(
                new Date(113, 5, 14).getTime(), 3, id);
        tt2.execute();

        // not a week day
        AddTimeCard tt3 = new AddTimeCard(
                new Date(113, 5, 16).getTime(), 4, id);
        tt3.execute();

        // last saturday, pay
        AddTimeCard tt4 = new AddTimeCard(
                new Date(113, 5, 8).getTime(), 4, id);
        tt4.execute();

        // not a week day
        AddTimeCard tt5 = new AddTimeCard(
            new Date(113, 5, 7).getTime(), 4, id);
        tt5.execute();


        // friday, pay day
        PaydayTransaction pdt = new PaydayTransaction(
                new Date(113, 5, 14).getTime());
        pdt.execute();

        PayCheck pc = pdt.getPayCheck(id);
        assertNotNull(pc);
        assertEquals(1100.0, pc.getGrossPay());
        assertEquals(1100.0, pc.getNetPay());
    }

    public void testSingleHourlyEmployeeOvertime() {
        String id = "empTSHE005";

        AddHourlyEmployee aht = new AddHourlyEmployee(id, "Bob", "Beijing", 100.0);
        aht.execute();
        assertNotNull(aht);

        // not working day, overtime, 1.5 * 1 * 100
        AddTimeCard sundayTrans = new AddTimeCard(
                new Date(113, 5, 9).getTime(), 1, id);
        sundayTrans.execute();

        // working day, 2 * 100
        AddTimeCard tuesdayTrans = new AddTimeCard(
                new Date(113, 5, 11).getTime(), 2, id);
        tuesdayTrans.execute();

        // last saturday, not working day, overtime, 1.5 * 4 * 100
        AddTimeCard saturdayTrans = new AddTimeCard(
                new Date(113, 5, 8).getTime(), 4, id);
        saturdayTrans.execute();

        // Saturday, weekend
        PaydayTransaction pdt = new PaydayTransaction(
                new Date(113, 5, 14).getTime());
        pdt.execute();

        PayCheck pc = pdt.getPayCheck(id);
        assertNotNull(pc);
        assertEquals(950.0, pc.getGrossPay());
        assertEquals(950.0, pc.getNetPay());
    }

    public void testSingleHourlyEmployeeOvertimeWeekday() {
        String id = "empTSHE008";

        AddHourlyEmployee aht = new AddHourlyEmployee(id, "Bob", "Beijing", 100.0);
        aht.execute();
        assertNotNull(aht);

        // overtime in weekday, 8 * 100 + 2 * 100 * 1.5
        AddTimeCard tt = new AddTimeCard(new Date(113, 5, 14).getTime(), 10, id);
        tt.execute();

        PaydayTransaction pdt = new PaydayTransaction(new Date(113, 5, 14).getTime());
        pdt.execute();

        PayCheck pc = pdt.getPayCheck(id);
        assertNotNull(pc);
        assertEquals(1100.0, pc.getGrossPay());
        assertEquals(1100.0, pc.getNetPay());
    }
}
