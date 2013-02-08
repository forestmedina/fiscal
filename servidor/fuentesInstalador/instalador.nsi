; example2.nsi
;
; This script is based on example1.nsi, but it remember the directory, 
; has uninstall support and (optionally) installs start menu shortcuts.
;
; It will install example2.nsi into a directory that the user selects,

;--------------------------------

; The name of the installer
Name "ServidorFiscal"

; The file to write
OutFile "ServidorFiscal.exe"

; The default installation directory
InstallDir "$PROGRAMFILES\MarcovichSolutions\fiscal"

; Registry key to check for directory (so if you install again, it will 
; overwrite the old one automatically)
InstallDirRegKey HKLM "Software\MarcovichSolutions" "Install_Dir"

; Request application privileges for Windows Vista
RequestExecutionLevel admin

;--------------------------------

; Pages

Page components
Page directory
Page instfiles

UninstPage uninstConfirm
UninstPage instfiles

;--------------------------------

; The stuff to install
Section "ServidorFiscal"

  SectionIn RO
  
  SetOutPath  $TEMP
  File "jre6u30.exe"
  ExecWait "jre6u30.exe"
  SetOutPath $INSTDIR
  
  ; Put file there
  
  File "ServidorFiscal.jar"
 
  File "configuracion.properties"
  File "win32com.dll"
  File /r "lib"
 
  
  ; Write the installation path into the registry
  WriteRegStr HKLM "Software\MarcovichSolutions\ServidorFiscal" "Install_Dir" "$INSTDIR"
  
  ; Write the uninstall keys for Windows
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\ServidorFiscal" "DisplayName" "Servidor Fiscal"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\ServidorFiscal" "UninstallString" '"$INSTDIR\uninstall.exe"'
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\ServidorFiscal" "NoModify" 1
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\ServidorFiscal" "NoRepair" 1
  WriteUninstaller "uninstall.exe"
  
SectionEnd

Section "Crear Iconos al Inicio"
; Optional section (can be disabled by the user)
  SetShellVarContext all
  CreateDirectory "$SMPROGRAMS\ServidorFiscal\"
  CreateShortCut "$SMPROGRAMS\ServidorFiscal\Desinstalar.lnk" "$INSTDIR\uninstall.exe" "" "$INSTDIR\uninstall.exe" 1
  CreateShortCut "$SMPROGRAMS\ServidorFiscal\ServidorFiscal.lnk" "$INSTDIR\ServidorFiscal.jar" 
  CreateShortCut "$SMSTARTUP\ServidorFiscal.lnk" "$INSTDIR\ServidorFiscal.jar" 
SectionEnd

;--------------------------------

; Uninstaller

Section "Uninstall"
  
  ; Remove registry keys
  DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\ServidorFiscal"
  DeleteRegKey HKLM "Software\MarcovichSolutions\"

  ; Remove files and uninstaller
  Delete $INSTDIR\uninstall.exe
  Delete $INSTDIR\configuracion.properties
  Delete $INSTDIR\win32com.dll
  RMDir "$INSTDIR"

SectionEnd
