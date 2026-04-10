package com.ptithcm.apt.adapters.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptithcm.apt.R;
import com.ptithcm.apt.models.profile.FamilyMemberResponse;

import java.util.List;

public class FamilyMemberAdapter extends RecyclerView.Adapter<FamilyMemberAdapter.FamilyMemberViewHolder> {

    private List<FamilyMemberResponse> familyMemberList;
    private OnItemClickListener listener;
    private boolean isExpanded = false;

    public interface OnItemClickListener {
        void onItemClick(FamilyMemberResponse familyMember);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public FamilyMemberAdapter(List<FamilyMemberResponse> familyMemberList) {
        this.familyMemberList = familyMemberList;
    }

    public void setExpanded(boolean expanded) {
        this.isExpanded = expanded;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FamilyMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_family_member, parent, false);
        return new FamilyMemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FamilyMemberViewHolder holder, int position) {
        FamilyMemberResponse member = familyMemberList.get(position);
        holder.iconPerson.setImageResource(R.drawable.ic_person);
        holder.tvMemberName.setText(member.getFullName() != null ? member.getFullName() : "---");
        holder.tvMemberRelation.setText(member.getRole() != null ? member.getRole() : "---");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(member);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Math.min(familyMemberList.size(), isExpanded ? Integer.MAX_VALUE : 3);
    }

    public static class FamilyMemberViewHolder extends RecyclerView.ViewHolder {
        ImageView iconPerson;
        TextView tvMemberName;
        TextView tvMemberRelation;

        public FamilyMemberViewHolder(@NonNull View itemView) {
            super(itemView);
            iconPerson = itemView.findViewById(R.id.icon_person);
            tvMemberName = itemView.findViewById(R.id.tv_member_name);
            tvMemberRelation = itemView.findViewById(R.id.tv_member_relation);
        }
    }
}
