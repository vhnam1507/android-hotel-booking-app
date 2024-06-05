package project.tttn.vuhoangnam_20h1120232_hotelbookingapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.R;

public class GroupDetailBottomSheet extends BottomSheetDialogFragment {
    private static final int MAX_ROOMS = 5;
    private static final int MIN_ROOMS = 1;
    private static final int MIN_ADULTS = 1;
    private static final int MIN_KIDS = 0;
    private static final int MAX_PEOPLE = 15;

    int adultCount = 1, roomCount = 1, kidCount = 0;

    public static GroupDetailBottomSheet newInstance(int roomCount, int adultCount, int kidCount) {
        GroupDetailBottomSheet fragment = new GroupDetailBottomSheet();
        Bundle args = new Bundle();
        args.putInt("roomCount", roomCount);
        args.putInt("adultCount", adultCount);
        args.putInt("kidCount", kidCount);
        fragment.setArguments(args);
        return fragment;
    }

    public interface BottomSheetListener {
        void onButtonSheetSubmit(String result);
    }

    private BottomSheetListener mListener;

    public void setBottomSheetListener(BottomSheetListener listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_group_detail, container, false);

        TextView txtv_adultquant = view.findViewById(R.id.txtv_adultquant);
        TextView txtv_roomquant = view.findViewById(R.id.txtv_roomquant);
        TextView txtv_kidquant = view.findViewById(R.id.txtv_kidquant);

        // Set initial values from savedInstanceState or arguments
        if (getArguments() != null) {
            roomCount = getArguments().getInt("roomCount", roomCount);
            adultCount = getArguments().getInt("adultCount", adultCount);
            kidCount = getArguments().getInt("kidCount", kidCount);
        }
        updateCounts(txtv_roomquant, txtv_adultquant, txtv_kidquant);

        // Initialize Buttons
        Button btnRoomInc = view.findViewById(R.id.btn_roominc);
        Button btnRoomDec = view.findViewById(R.id.btn_roomdec);
        Button btnAdultInc = view.findViewById(R.id.btn_adultinc);
        Button btnAdultDec = view.findViewById(R.id.btn_adultdec);
        Button btnKidInc = view.findViewById(R.id.btn_kidinc);
        Button btnKidDec = view.findViewById(R.id.btn_kiddec);

        // Setup increment and decrement listeners
        btnRoomInc.setOnClickListener(v -> updateRoomCount(txtv_roomquant, true));
        btnRoomDec.setOnClickListener(v -> updateRoomCount(txtv_roomquant, false));
        btnAdultInc.setOnClickListener(v -> updateAdultCount(txtv_adultquant, txtv_kidquant, true));
        btnAdultDec.setOnClickListener(v -> updateAdultCount(txtv_adultquant, txtv_kidquant, false));
        btnKidInc.setOnClickListener(v -> updateKidCount(txtv_kidquant, txtv_adultquant, true));
        btnKidDec.setOnClickListener(v -> updateKidCount(txtv_kidquant, txtv_adultquant, false));

        Button btnPeopleDone = view.findViewById(R.id.btn_peopleDone);
        btnPeopleDone.setOnClickListener(v -> {
            String result = roomCount + " phòng - " + adultCount + " người lớn - " + kidCount + " trẻ em";
            if (mListener != null) {
                mListener.onButtonSheetSubmit(result);
            }
            dismiss();
        });

        return view;
    }

    private void updateCounts(TextView roomView, TextView adultView, TextView kidView) {
        if (roomView != null) roomView.setText(String.valueOf(roomCount));
        if (adultView != null) adultView.setText(String.valueOf(adultCount));
        if (kidView != null) kidView.setText(String.valueOf(kidCount));
    }

    private void updateRoomCount(TextView roomView, boolean increment) {
        if (increment) {
            if (roomCount < MAX_ROOMS) {
                roomCount++;
                updateCounts(roomView, null, null);
            }
        } else {
            if (roomCount > MIN_ROOMS) {
                roomCount--;
                updateCounts(roomView, null, null);
            }
        }
    }

    private void updateAdultCount(TextView adultView, TextView kidView, boolean increment) {
        if (increment) {
            if (adultCount + kidCount < MAX_PEOPLE) {
                adultCount++;
                updateCounts(null, adultView, null);
            }
        } else {
            if (adultCount > MIN_ADULTS) {
                adultCount--;
                updateCounts(null, adultView, null);
            }
        }
    }

    private void updateKidCount(TextView kidView, TextView adultView, boolean increment) {
        if (increment) {
            if (adultCount + kidCount < MAX_PEOPLE) {
                kidCount++;
                updateCounts(null, null, kidView);
            }
        } else {
            if (kidCount > MIN_KIDS) {
                kidCount--;
                updateCounts(null, null, kidView);
            }
        }
    }
}
