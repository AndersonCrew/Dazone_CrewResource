package com.kunpark.resource.view.add_schedule

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.kunpark.resource.base.BaseActivity
import com.kunpark.resource.databinding.ActivityOrganizationBinding
import com.kunpark.resource.model.Organization
import com.kunpark.resource.model.User
import com.kunpark.resource.utils.Constants
import com.kunpark.resource.utils.DazoneApplication
import com.kunpark.resource.view.organization.OrganizationAdapter
import kotlinx.coroutines.*
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class OrganizationActivity : BaseActivity() {
    private var binding: ActivityOrganizationBinding? = null
    private var adapter: OrganizationAdapter? = null
    private var data: ArrayList<Organization> = arrayListOf()
    private var viewModel: OrganizationViewModel?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityOrganizationBinding.inflate(layoutInflater)
        viewModel = ViewModelProviders.of(this).get(OrganizationViewModel::class.java)
        setContentView(binding?.root)
        initViewTemp()
        super.onCreate(savedInstanceState)
    }

    private fun initViewTemp() {
        showProgressDialog()

        getIntentSelected()

        binding?.icBack?.setOnClickListener {
            onBackPressed()
        }

        binding?.icDone?.setOnClickListener {
            val intent = Intent()
            intent.putExtra(Constants.SELECTED_CHOSEN, Gson().toJson(selected))
            setResult(RESULT_OK, intent)
            finish()

        }
    }

    override fun initEvent() {

    }

    override fun initViewModel() {

    }

    override fun onDestroy() {
        super.onDestroy()
        GlobalScope.launch {
            resetChecked(null)
        }
    }

    override fun initView() {

    }

    private suspend fun resetChecked(selected: ArrayList<Organization>?) {
        val scope = CoroutineScope(Dispatchers.IO)
        val job = scope.launch {
            data.forEach {
                Log.d("RECHECK", "resetChecked ${Thread.currentThread()}")
                it.isChosen = false
                it.childMembers.forEach { member -> member.isChosen = false }

                it.childDepartments.forEach { department ->
                    withContext(Dispatchers.IO) {
                        reSetDepartment(department)
                    }
                }
            }
        }

        job.join()
        Log.d("RECHECK", "resetChecked DONE")
        DazoneApplication.getInstance().mPref?.putListOrganization(data)


        if(selected != null ) {
            val intent = Intent()
            intent.putExtra(Constants.BUNDLE_LIST_PERSON, Gson().toJson(selected))
            setResult(RESULT_OK, intent)
            finish()
        }

    }

    private suspend fun reSetDepartment(personData: Organization) {
        Log.d("RECHECK", "reSetDepartment ${Thread.currentThread()}")
        personData.isChosen = false
        personData.childMembers.forEach { member -> member.isChosen = false }

        personData.childDepartments.forEach { department ->
            withContext(Dispatchers.IO) {
                reSetDepartment(department)
            }
        }
    }

    private fun getIntentSelected() = GlobalScope.launch {
        Log.d("RECHECK", "getIntentSelected")
        if(!DazoneApplication.getInstance().mPref?.getListOrganization().isNullOrEmpty()) {
            data = ArrayList(DazoneApplication.getInstance().mPref?.getListOrganization())
            intent.getStringExtra(Constants.SELECTED_LIST)?.let {
                val selects = Gson().fromJson<ArrayList<User>>(
                    intent.getStringExtra(Constants.SELECTED_LIST),
                    object : TypeToken<ArrayList<User?>?>() {}.type
                )

                Log.d("RECHECK", "getIntentSelected size selected = ${selects.size}")
                if (!selects.isNullOrEmpty()) {
                    //TODO Update list
                    val scope = CoroutineScope(Dispatchers.IO)
                    val job = scope.launch {
                        selects.forEach {
                            Log.d("RECHECK", "getIntentSelected forEach")
                            updateSelected(it, data)
                        }
                    }

                    job.join()
                    DazoneApplication.getInstance().mPref?.putListOrganization(data)
                    withContext(Dispatchers.Main) {
                        initRecyclerView()
                    }
                } else if(!data.isNullOrEmpty()) {
                    withContext(Dispatchers.Main) {
                        initRecyclerView()
                    }
                }
            }
        }
    }

    private fun initRecyclerView() {
        adapter = OrganizationAdapter({ organization, isChecked ->
            GlobalScope.launch {
                checkOrganization(organization, isChecked)
            }
        }, { department, member, isChecked ->
            GlobalScope.launch {
                if(department != null) {
                    checkMember(department, member, isChecked)
                }
            }
        })

        binding?.rvOrganization?.adapter = adapter
        adapter?.submitList(data)
        Handler(Looper.getMainLooper()).postDelayed({
            dismissProgressDialog()
        }, 2000)

        GlobalScope.launch {
            selected.clear()
            val listSelected: Deferred<ArrayList<User>> = async { adapter!!.getSelected() }
            selected.addAll( listSelected.await())
        }
    }

    private suspend fun checkMember(department: Organization, member: User, isChecked: Boolean) {
        val job = GlobalScope.launch {
            val iterator = data.iterator()
            while (iterator.hasNext()) {
                val organization = iterator.next()
                if(organization.departNo == department.departNo) {
                    Log.d("CHOOSE_DEPARTMENT", "Found Department ${department.departNo} - ${organization.departNo}")
                    if(!organization.childMembers.isNullOrEmpty()) {
                        organization.childMembers.find { it.userNo == member.userNo }?.let { memberChild ->
                            memberChild.isChosen = isChecked
                            if(isChecked) {
                                if(selected.find { it.userNo == member.userNo } == null) {
                                    selected.add(memberChild)
                                }
                            } else {
                                selected.find { it.userNo == member.userNo }?.let {
                                    selected.remove(memberChild)
                                }
                            }
                            Log.d("CHOOSE_DEPARTMENT", "Found Member ${memberChild.userNo} - ${memberChild.isChosen}")
                            return@launch
                        }
                    }
                } else {
                    if(!organization.childDepartments.isNullOrEmpty()) {
                        withContext(Dispatchers.IO) {
                            checkChooseMember(department, member, organization.childDepartments, isChecked)
                        }
                    }
                }
            }
        }

        job.join()
        Log.d("CHOOSE_DEPARTMENT", " Choose member DONE Selected size = ${selected.size}")
        withContext(Dispatchers.Main) {
            adapter?.submitList(data)
            adapter?.notifyDataSetChanged()
        }
    }

    private fun checkChooseMember(department: Organization, member: User, deparments: ArrayList<Organization>, isChecked: Boolean) {
        Log.d("CHOOSE_DEPARTMENT", "checkChooseMember")
        val iterator = deparments.iterator()
        while (iterator.hasNext()) {
            val organization = iterator.next()
            if(organization.departNo == department.departNo) {
                Log.d("CHOOSE_DEPARTMENT", "Found Department ${department.departNo} - ${organization.departNo}")
                if(!organization.childMembers.isNullOrEmpty()) {
                    organization.childMembers.find { it.userNo == member.userNo }?.let { memberChild ->
                        memberChild.isChosen = isChecked

                        if(isChecked) {
                            if(selected.find { it.userNo == member.userNo } == null) {
                                selected.add(memberChild)
                            }
                        } else {
                            selected.find { it.userNo == member.userNo }?.let {
                                selected.remove(memberChild)
                            }
                        }
                        Log.d("CHOOSE_DEPARTMENT", "Found Member ${memberChild.userNo} - ${memberChild.isChosen}")
                        return
                    }
                }
            } else {
                if(!organization.childDepartments.isNullOrEmpty()) {
                    checkChooseMember(department, member, organization.childDepartments, isChecked)
                }
            }
        }
    }

    private fun updateSelected(personData: User, listCheck: ArrayList<Organization>) {
        listCheck.forEach {
            Log.d("RECHECK", "updateSelected forEach ${listCheck.size}")
            if(!it.childMembers.filter { member -> member.userID == personData.userID }.isNullOrEmpty()) {
                Log.d("RECHECK", "Has Member ${it.childMembers.filter { member -> member.userID == personData.userID }.size}")
                val iterator = it.childMembers.filter { member -> member.userID == personData.userID }.iterator()
                while (iterator.hasNext()) {
                    var member = iterator.next()
                    member.isChosen = true
                }
            }

            if(!it.childDepartments.isNullOrEmpty()) {
                Log.d("RECHECK", "updateSelected PersonList ${it.childDepartments.size}")
                updateSelected(personData, it.childDepartments)
            }
        }
    }

    private val selected: ArrayList<User> = arrayListOf()
    private suspend fun checkOrganization(organization: Organization, checked: Boolean) {
        Log.d("CHOOSE_DEPARTMENT", "START checkOrganization")
        val job = GlobalScope.launch {
            organization.isChosen = checked

            organization.childMembers.forEach { member ->
                member.isChosen = checked
                if(checked) {
                    if(selected.find { it.userNo == member.userNo } == null) {
                        selected.add(member)
                    }
                } else {
                    selected.find { it.userNo == member.userNo }?.let {
                        selected.remove(member)
                    }

                }


                Log.d("CHOOSE_DEPARTMENT", "Found member ${member.fullName} checkOrganization")
            }

            if(!organization.childDepartments.isNullOrEmpty()) {
                organization.childDepartments.forEach { department ->
                    withContext(Dispatchers.IO) {
                        checkDepartment(checked, department)
                    }
                }
            }
        }

        job.join()
        Log.d("CHOOSE_DEPARTMENT", "DONE checkOrganization")
        updateDataAdapter(organization)
    }

    private suspend fun checkDepartment(checked: Boolean, department: Organization) {
        Log.d("CHOOSE_DEPARTMENT", "checkDepartment checkOrganization ${department.name}")
        department.isChosen = checked

        department.childMembers.forEach { member ->
            member.isChosen = checked
            if(checked) {
                if(selected.find { it.userNo == member.userNo } == null) {
                    selected.add(member)
                }
            } else {
                selected.find { it.userNo == member.userNo }?.let {
                    selected.remove(member)
                }
            }
            Log.d("CHOOSE_DEPARTMENT", "Found member ${member.fullName} checkOrganization")
        }

        if(!department.childDepartments.isNullOrEmpty()) {
            department.childDepartments.forEach { department ->
                withContext(Dispatchers.IO) {
                    checkDepartment(checked, department)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private suspend fun updateDataAdapter(organization: Organization) {
        val job = GlobalScope.launch {
            val iterator = data.iterator()
            while (iterator.hasNext()) {
                val department = iterator.next()
                if(organization.departNo == department.departNo) {
                    department.isChosen = organization.isChosen
                    department.childMembers = ArrayList(organization.childMembers)
                    department.childDepartments = ArrayList(organization.childDepartments)

                    Log.d("CHOOSE_DEPARTMENT", "Found Department ${department.departNo} - ${organization.departNo}")
                    return@launch
                } else {
                    if(!department.childDepartments.isNullOrEmpty()) {
                        checkUpdateDepart(organization, department.childDepartments)
                    }
                }
            }
        }

        job.join()
        Log.d("CHOOSE_DEPARTMENT", "DONE updateDataAdapter")
        Log.d("CHOOSE_DEPARTMENT", "Selected size = ${selected.size}")

        withContext(Dispatchers.Main) {
            adapter?.submitList(data)
            adapter?.notifyDataSetChanged()
        }

    }

    private fun checkUpdateDepart(organization: Organization, personList: ArrayList<Organization>) {
        Log.d("CHOOSE_DEPARTMENT", "Check child departments")
        val iterator = personList.iterator()
        while (iterator.hasNext()) {
            val department = iterator.next()
            if(organization.departNo == department.departNo) {
                department.isChosen = organization.isChosen
                department.childMembers = ArrayList(organization.childMembers)
                department.childDepartments = ArrayList(organization.childDepartments)

                Log.d("CHOOSE_DEPARTMENT", "Found Department ${department.departNo} - ${organization.departNo}")
                return
            } else {
                checkUpdateDepart(organization, department.childDepartments)
            }
        }
    }
}