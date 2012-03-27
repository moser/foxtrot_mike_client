package fmclient.views

import swing._
import java.text.DateFormat
import java.awt.Insets
import javax.swing.{JFormattedTextField, Icon, BorderFactory}
import fmclient.models.repos._
import fmclient.models._
import fmclient.views.AutoCompleter._
import java.awt.Color
import com.wordpress.tips4java.ComponentBorder


class MyFormattedTextField(f: javax.swing.JFormattedTextField.AbstractFormatterFactory) extends FormattedTextField(null) {
  override lazy val peer: JFormattedTextField = new JFormattedTextField(f) with SuperMixin {
    override def processFocusEvent(e:java.awt.event.FocusEvent) {
      super.processFocusEvent(e)
      selectAll
    }
  }
}


class FlightView extends MigPanel("fillx, gap 4!", "[gp 0][gp 0][gp 1][gp 1][gp 1][gp 1][gp 0][gp 0][gp 0][gp 0][gp 1]") {
  val departureDate = new FormattedTextField(DateFormat.getDateInstance(DateFormat.SHORT)) {
    focusLostBehavior = FormattedTextField.FocusLostBehavior.CommitOrRevert
  }
  val plane = new AutoCompleter(new EnabledOnlyAutoCompleterModel[Plane](AllPlanes, _.registration, Map("allowNil" -> false)), new PlaneRenderer)
  val seat1 = new AutoCompleter(new Seat1ACModel)
  val seat2 = new AutoCompleter(new Seat2ACModel)
  val airfieldRenderer = new AirfieldRenderer
  val from = new AutoCompleter(new EnabledOnlyAutoCompleterModel[Airfield](AllAirfields, (o:Airfield) => o.name + " " + o.registration, Map("allowNil" -> false)), airfieldRenderer)
  val to = new AutoCompleter(new EnabledOnlyAutoCompleterModel[Airfield](AllAirfields, (o:Airfield) => o.name + " " + o.registration, Map("allowNil" -> false)), airfieldRenderer)

  val btDepartureTime = new InnerButton()
  val departureTime = new MyFormattedTextField(TimeFormatterFactory)
  new ComponentBorder(btDepartureTime.peer).install(departureTime.peer)
  val btArrivalTime = new InnerButton()
  val arrivalTime = new MyFormattedTextField(TimeFormatterFactory)
  new ComponentBorder(btArrivalTime.peer).install(arrivalTime.peer)
  val duration = new TextField {
    enabled = false
  }
  val engineDuration = new MyFormattedTextField(TimeFormatterFactory)
  val controller = new AutoCompleter(new DefaultAutoCompleterModel[Person](AllPeople, _.name, Map("allowNil" -> true)))
  val launchType = new ComboBox(List[LaunchItem](LaunchItem("self_launch"),
                                                  LaunchItem("wire_launch"),
                                                  LaunchItem("tow_launch")))
  protected var _launchPanel : MigPanel = _
  protected var launchPanelWrapper = new MigPanel("ins 0, fill")
  def launchPanel = _launchPanel
  def launchPanel_=(p : MigPanel) = {
    if(_launchPanel != null) {
      launchPanelWrapper.remove(_launchPanel)
    }
    _launchPanel = p
    launchPanelWrapper.add(_launchPanel, "grow")
    revalidate
  }
  launchPanel = new MigPanel("ins 0")
  val liabilitiesPanel = new MigPanel("ins 0")
  val liability = List(new CheckBox(), new CheckBox(), new CheckBox(), new CheckBox())
  val liablePerson = List(new MyComboBox[Person](new MyComboBoxModel(AllPeople)),
                          new MyComboBox[Person](new MyComboBoxModel(AllPeople)),
                          new MyComboBox[Person](new MyComboBoxModel(AllPeople)),
                          new MyComboBox[Person](new MyComboBoxModel(AllPeople)))
  val proportion = List(new ComboBox(List[ProportionItem](ProportionItem(1), ProportionItem(2), ProportionItem(3), ProportionItem(4))),
                                  new ComboBox(List[ProportionItem](ProportionItem(1), ProportionItem(2), ProportionItem(3), ProportionItem(4))),
                                  new ComboBox(List[ProportionItem](ProportionItem(1), ProportionItem(2), ProportionItem(3), ProportionItem(4))),
                                  new ComboBox(List[ProportionItem](ProportionItem(1), ProportionItem(2), ProportionItem(3), ProportionItem(4))))
  val realProportion = List(new Label(""), new Label(""), new Label(""), new Label(""))
  val controlPanel = new MigPanel("ins 0")
  val btSave = new Button(I18n("save"))
  val btDelete = new Button(I18n("delete"))

  val costHint = new MyComboBox[CostHint](new MyComboBoxModel(AllCostHints, true))
  val comment = new TextField

  add(new Label(I18n("departure_date")))
  add(new Label(I18n("plane")))
  add(new Label(I18n("seat1")))
  add(new Label(I18n("seat2")))
  add(new Label(I18n("from")))
  add(new Label(I18n("to")))
  add(new Label(I18n("departure_time")))
  add(new Label(I18n("arrival_time")))
  add(new Label(I18n("duration")))
  add(new Label(I18n("engine_duration")))
  add(new Label(I18n("controller")), "wrap")

  add(departureDate, "w 70::")
  peer.add(plane, "w 80::")
  peer.add(seat1, "w 110::, sg names, grow")
  peer.add(seat2, "sg names, grow")
  peer.add(from, "w 70::, sg airfields, grow")
  peer.add(to, "sg airfields, grow")
  add(departureTime, "w 70::, sg times")
  add(arrivalTime, "sg times")
  add(duration, "w 55::, sg durations")
  add(engineDuration, "sg durations")
  peer.add(controller, "sg names, grow, wrap")

  add(launchType)
  add(launchPanelWrapper, "gap 0, grow, spanx, wrap")
  add(liabilitiesPanel, "grow, spanx, wrap")
  liabilitiesPanel.add(new Label(I18n("liabilities")))
  for(i <- 0 to 3) {
    liabilitiesPanel.add(liability(i))
    liabilitiesPanel.add(liablePerson(i), "w 160::")
    liabilitiesPanel.add(proportion(i))
    if(i == 1) {
      liabilitiesPanel.add(realProportion(i), "w 40::, wrap")
      liabilitiesPanel.add(new Label(""))
    } else {
      liabilitiesPanel.add(realProportion(i), "w 40::")
    }

  }

  add(controlPanel, "spanx 10")
  controlPanel.add(btSave)
  controlPanel.add(btDelete)
  controlPanel.add(new Label(I18n("cost_hint")))
  controlPanel.add(costHint)
  controlPanel.add(new Label(I18n("comment")))
  controlPanel.add(comment, "w 150::, grow")

  var _enabled = true
  override def enabled = _enabled
  override def enabled_=(b:Boolean) = {
    _enabled = b
    departureDate.enabled = b
    btDepartureTime.enabled = b
    departureTime.enabled = b
    btArrivalTime.enabled = b
    arrivalTime.enabled = b
    engineDuration.enabled = b
    launchType.enabled = b
    btSave.enabled = b
    btDelete.enabled = b
    costHint.enabled = b
    comment.enabled = b

    plane.setEnabled(b)
    seat1.setEnabled(b)
    seat2.setEnabled(b)
    controller.setEnabled(b)
    from.setEnabled(b)
    to.setEnabled(b)

    _launchPanel.enabled = b
    if(!b) {
      for(i <- 0 to 3) {
        liability(i).enabled = b
        liablePerson(i).enabled = b
        proportion(i).enabled = b
      }
    }
  }
}
