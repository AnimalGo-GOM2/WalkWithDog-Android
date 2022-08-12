package rocateer.animalgo.activity.walk;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.github.florent37.shapeofview.shapes.RoundRectView;
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver;
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager;
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendar;
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter;
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.WalkModel;
import timber.log.Timber;

public class SelectDateActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context, WalkModel walkModel) {
    Intent intent = new Intent(context, SelectDateActivity.class);
    mWalkModel = walkModel;
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.back_button)
  AppCompatImageView mBackbutton;
  @BindView(R.id.single_row_calendar)
  SingleRowCalendar mSingleRowCalendar;
  @BindView(R.id.year_text_view)
  AppCompatTextView mYearTextView;
  @BindView(R.id.hour_spinner)
  PowerSpinnerView mHourSpinner;
  @BindView(R.id.selected_date_text_view)
  AppCompatTextView mSelectedDateTextView;
  @BindView(R.id.selected_time_text_view)
  AppCompatTextView mSelectedTimeTextView;
  @BindView(R.id.minute_spinner)
  PowerSpinnerView mMinuteSpinner;
  @BindView(R.id.selected_minute_text_view)
  AppCompatTextView mSelectedMinuteTextView;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private SelectDateAdapter mSelectDateAdapter;
  private static WalkModel mWalkModel;
  private static String mHour = "";
  private static String mMinute = "";
  private static String mYear = "";
  private static String mMonth = "";
  private static String mDay = "";
  ArrayList<BaseModel> mSelectDateList = new ArrayList<>();
  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_select_date;
  }

  @Override
  protected void initLayout() {
    initToolbar("산책친구 등록");
    mBackbutton.setVisibility(View.GONE);
    initCalendar();


    mHourSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
      @Override
      public void onItemSelected(int oldIndex, @Nullable String oldItem, int newIndex, String newItem) {
        if (mHourSpinner.getSelectedIndex() == 0) {
          mSelectedTimeTextView.setText("00시(오전12시)");
          mHour = "00";
        } else if (mHourSpinner.getSelectedIndex() == 1) {
          mSelectedTimeTextView.setText("01시(오전01시)");
          mHour = "01";
        } else if (mHourSpinner.getSelectedIndex() == 2) {
          mSelectedTimeTextView.setText("02시(오전02시)");
          mHour = "02";
        } else if (mHourSpinner.getSelectedIndex() == 3) {
          mSelectedTimeTextView.setText("03시(오전03시)");
          mHour = "03";
        } else if (mHourSpinner.getSelectedIndex() == 4) {
          mSelectedTimeTextView.setText("04시(오전04시)");
          mHour = "04";
        } else if (mHourSpinner.getSelectedIndex() == 5) {
          mSelectedTimeTextView.setText("05시(오전05시)");
          mHour = "05";
        } else if (mHourSpinner.getSelectedIndex() == 6) {
          mSelectedTimeTextView.setText("06시(오전06시)");
          mHour = "06";
        } else if (mHourSpinner.getSelectedIndex() == 7) {
          mSelectedTimeTextView.setText("07시(오전07시)");
          mHour = "07";
        } else if (mHourSpinner.getSelectedIndex() == 8) {
          mSelectedTimeTextView.setText("08시(오전08시)");
          mHour = "08";
        } else if (mHourSpinner.getSelectedIndex() == 9) {
          mSelectedTimeTextView.setText("09시(오전09시)");
          mHour = "09";
        } else if (mHourSpinner.getSelectedIndex() == 10) {
          mSelectedTimeTextView.setText("10시(오전10시)");
          mHour = "10";
        } else if (mHourSpinner.getSelectedIndex() == 11) {
          mSelectedTimeTextView.setText("11시(오전11시)");
          mHour = "11";
        } else if (mHourSpinner.getSelectedIndex() == 12) {
          mSelectedTimeTextView.setText("12시(오후12시)");
          mHour = "12";
        } else if (mHourSpinner.getSelectedIndex() == 13) {
          mSelectedTimeTextView.setText("13시(오후01시)");
          mHour = "13";
        } else if (mHourSpinner.getSelectedIndex() == 14) {
          mSelectedTimeTextView.setText("14시(오후02시)");
          mHour = "14";
        } else if (mHourSpinner.getSelectedIndex() == 15) {
          mSelectedTimeTextView.setText("15시(오후03시)");
          mHour = "15";
        } else if (mHourSpinner.getSelectedIndex() == 16) {
          mSelectedTimeTextView.setText("16시(오후04시)");
          mHour = "16";
        } else if (mHourSpinner.getSelectedIndex() == 17) {
          mSelectedTimeTextView.setText("17시(오후05시)");
          mHour = "17";
        } else if (mHourSpinner.getSelectedIndex() == 18) {
          mSelectedTimeTextView.setText("18시(오후06시)");
          mHour = "18";
        } else if (mHourSpinner.getSelectedIndex() == 19) {
          mSelectedTimeTextView.setText("19시(오후07시)");
          mHour = "19";
        } else if (mHourSpinner.getSelectedIndex() == 20) {
          mSelectedTimeTextView.setText("20시(오후08시)");
          mHour = "20";
        } else if (mHourSpinner.getSelectedIndex() == 21) {
          mSelectedTimeTextView.setText("21시(오후09시)");
          mHour = "21";
        } else if (mHourSpinner.getSelectedIndex() == 22) {
          mSelectedTimeTextView.setText("22시(오후10시)");
          mHour = "22";
        } else if (mHourSpinner.getSelectedIndex() == 23) {
          mSelectedTimeTextView.setText("23시(오후11시)");
          mHour = "23";
        }
        Timber.i(mHour);
      }
    });

    mMinuteSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
      @Override
      public void onItemSelected(int oldIndex, @Nullable String oldItem, int newIndex, String newItem) {
        if (mMinuteSpinner.getSelectedIndex() == 0) {
          mSelectedMinuteTextView.setText("00분");
          mMinute = "00";
        } else if (mMinuteSpinner.getSelectedIndex() == 1) {
          mSelectedMinuteTextView.setText("10분");
          mMinute = "10";
        } else if (mMinuteSpinner.getSelectedIndex() == 2) {
          mSelectedMinuteTextView.setText("20분");
          mMinute = "20";
        } else if (mMinuteSpinner.getSelectedIndex() == 3) {
          mSelectedMinuteTextView.setText("30분");
          mMinute = "30";
        } else if (mMinuteSpinner.getSelectedIndex() == 4) {
          mSelectedMinuteTextView.setText("40분");
          mMinute = "40";
        } else if (mMinuteSpinner.getSelectedIndex() == 5) {
          mSelectedMinuteTextView.setText("50분");
          mMinute = "50";
        }
        Timber.i(mMinute);
      }
    });

  }

  @Override
  protected void initRequest() {

  }


  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   * 달력 커스터마이징
   */
  private void initCalendar() {

    CalendarViewManager calendarViewManager = new CalendarViewManager() {
      @Override
      public int setCalendarViewResourceId(int i, @NonNull Date date, boolean b) {
        return R.layout.row_calendar;
      }

      @Override
      public void bindDataToCalendarView(@NonNull SingleRowCalendarAdapter.CalendarViewHolder calendarViewHolder, @NonNull Date date, int i, boolean b) {
        String dayOfTheWeek = (String) DateFormat.format("E", date); // Thursday
        String day = (String) DateFormat.format("dd", date); // 20
        String monthString = (String) DateFormat.format("MMM", date); // Jun
        String monthNumber = (String) DateFormat.format("MM", date); // 06
        String year = (String) DateFormat.format("yyyy", date); // 2013

        AppCompatTextView dateTextView = calendarViewHolder.itemView.findViewById(R.id.tv_day_calendar_item);
        dateTextView.setText(day);

        AppCompatTextView dayCalendarTextView = calendarViewHolder.itemView.findViewById(R.id.tv_date_calendar_item);
        dayCalendarTextView.setText(dayOfTheWeek);

        mYearTextView.setText(year + "년" + monthNumber + "월");


        LinearLayout clCalendarItem = calendarViewHolder.itemView.findViewById(R.id.cl_calendar_item);



        RoundRectView roundView = calendarViewHolder.itemView.findViewById(R.id.round_view);

        if (mSingleRowCalendar.getSelectedIndexes().contains(i)) {
          roundView.setBorderWidthPx(Tools.getInstance().dpTopx(mActivity, 1));
          dateTextView.setBackgroundColor(getColor(R.color.color_28E0AB0A));
        } else {
          roundView.setBorderWidthPx(Tools.getInstance().dpTopx(mActivity, 0));
          dateTextView.setBackgroundColor(getColor(R.color.color_ffffff));
        }




      }
    };

    CalendarSelectionManager calendarSelectionManager = new CalendarSelectionManager() {
      @Override
      public boolean canBeItemSelected(int i, @NonNull Date date) {
        return true;
      }
    };

    CalendarChangesObserver calendarChangesObserver = new CalendarChangesObserver() {
      @Override
      public void whenWeekMonthYearChanged(@NonNull String s, @NonNull String s1, @NonNull String s2, @NonNull String s3, @NonNull Date date) {
        Timber.i("date = " + date);
      }

      @Override
      public void whenSelectionChanged(boolean b, int i, @NonNull Date date) {
        Timber.i("Select Date = " + date);
        String dayOfTheWeek = (String) DateFormat.format("E", date); // Thursday
        String day = (String) DateFormat.format("dd", date); // 20
        String monthString = (String) DateFormat.format("MMM", date); // Jun
        String monthNumber = (String) DateFormat.format("MM", date); // 06
        String year = (String) DateFormat.format("yyyy", date); // 2013

        mYear = year;
        mMonth = monthNumber;
        mDay = day;


        mSelectedDateTextView.setText(year + "." + monthNumber + "." + day);
      }

      @Override
      public void whenCalendarScrolled(int i, int i1) {

      }

      @Override
      public void whenSelectionRestored() {

      }

      @Override
      public void whenSelectionRefreshed() {

      }
    };

    mSingleRowCalendar.setCalendarViewManager(calendarViewManager);
    mSingleRowCalendar.setCalendarSelectionManager(calendarSelectionManager);
    mSingleRowCalendar.setCalendarChangesObserver(calendarChangesObserver);
    mSingleRowCalendar.setFutureDaysCount(21);
    mSingleRowCalendar.setIncludeCurrentDate(true);
    mSingleRowCalendar.init();

    calendarChangesObserver.whenSelectionChanged(false, 0 , new Date());
    mSingleRowCalendar.select(0);
  }



  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------

  /**
   * 다음 버튼
   */
  @OnClick(R.id.next_button)
  public void NextTouched() {
    Timber.i(mWalkModel.getRecord_date());
    mWalkModel.setRecord_date(mYear + "-" + mMonth + "-" + mDay+ " " + mHour + ":" + mMinute);
    Intent selectLocationActivity = SelectLocationActivity.getStartIntent(mActivity, mWalkModel);
    startActivity(selectLocationActivity, TRANS.PRESENT);
    finishWithRemove();
  }

  /**
   * 취소
   */
  @OnClick(R.id.walk_cancle_button)
  public void walkCancleTouched() {
    finishWithRemove();
  }


}
