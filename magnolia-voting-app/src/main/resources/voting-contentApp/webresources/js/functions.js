function enableDisableTextField(otherRadioBtnId, voterOwnAnswerId) {
  var otherRadioBtn = document.getElementById(otherRadioBtnId);
  var answerTextField = document.getElementById(voterOwnAnswerId);
  
  answerTextField.disabled = otherRadioBtn.checked ? false : true;
  
  if (!answerTextField.disabled) {
    answerTextField.focus();
  }
}